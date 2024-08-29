package org.g9project4.board.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.g9project4.board.controllers.AdminBoardDataSearch;
import org.g9project4.board.controllers.BoardDataSearch;
import org.g9project4.board.controllers.RequestBoard;
import org.g9project4.board.entities.*;
import org.g9project4.board.exceptions.BoardDataNotFoundException;
import org.g9project4.board.exceptions.BoardNotFoundException;
import org.g9project4.board.repositories.BoardDataRepository;
import org.g9project4.board.repositories.BoardRepository;
import org.g9project4.board.services.comment.CommentInfoService;
import org.g9project4.file.entities.FileInfo;
import org.g9project4.file.services.FileInfoService;
import org.g9project4.global.CommonSearch;
import org.g9project4.global.ListData;
import org.g9project4.global.Pagination;
import org.g9project4.global.Utils;
import org.g9project4.global.constants.DeleteStatus;
import org.g9project4.member.MemberUtil;
import org.g9project4.member.constants.Authority;
import org.g9project4.member.entities.Member;
import org.g9project4.wishlist.constants.WishType;
import org.g9project4.wishlist.services.WishListService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardInfoService {

    private final JPAQueryFactory queryFactory;
    private final BoardDataRepository repository;
    private final BoardConfigInfoService configInfoService;
    private final CommentInfoService commentInfoService;
    private final FileInfoService fileInfoService;

    private final BoardRepository boardRepository;
    private final HttpServletRequest request;
    private final ModelMapper modelMapper;
    private final MemberUtil memberUtil;
    private final Utils utils;
    private final WishListService wishListService;

    public List<Board> getBoardList() {
        return Objects.requireNonNullElse(boardRepository.findAll(Sort.by(desc("listOrder"))).stream().toList(), Collections.EMPTY_LIST);
    }

    /**
     * 게시글 목록 조회
     *
     * @return
     */
    public ListData<BoardData> getList(BoardDataSearch search, DeleteStatus status) {


        String bid = search.getBid();
        List<String> bids = search.getBids(); // 게시판 여러개 조회

        // 게시판 설정 조회
        Board board = bid != null && StringUtils.hasText(bid.trim()) ? configInfoService.get(bid.trim()).orElseThrow(BoardNotFoundException::new) : new Board();

        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit > 0 ? limit : board.getRowsPerPage();

        int offset = (page - 1) * limit;

        // 삭제가 되지 않은 게시글 목록이 기본 값
        status = Objects.requireNonNullElse(status, DeleteStatus.UNDELETED);

        String sopt = search.getSopt();
        String skey = search.getSkey();

        List<String> categories = search.getCategory();
        Boolean notice = search.getNotice();

        /* 검색 처리 S */
        QBoardData boardData = QBoardData.boardData;
        BooleanBuilder andBuilder = new BooleanBuilder();
        /* 관리자 검색 용도 */
        if (memberUtil.isAdmin() && search instanceof AdminBoardDataSearch adminSearch) {
            List<Long> memberSeq = adminSearch.getMemberSeq();
            if (memberSeq != null && !memberSeq.isEmpty()) {
                andBuilder.and(boardData.member.seq.in(memberSeq));
            }
        }
        // 삭제, 미삭제 게시글 조회 처리
        if (status != DeleteStatus.ALL) {
            if (status == DeleteStatus.UNDELETED) {
                andBuilder.and(boardData.deletedAt.isNull()); // 미삭된 게시글
            } else {
                andBuilder.and(boardData.deletedAt.isNotNull()); // 삭제된 게시글
            }
        }

        if (bid != null && StringUtils.hasText(bid.trim())) { // 게시판별 조회
            bids = List.of(bid);
        }

        if (bids != null && !bids.isEmpty()) { // 게시판 여러개 조회
            andBuilder.and(boardData.board.bid.in(bids));
        }

        // 분류 검색 처리
        if (categories != null && !categories.isEmpty()) {
            andBuilder.and(boardData.category.in(categories));
        }

        // num1 검색 추가
        Long num1 = search.getNum1();
        if (num1 != null) {
            andBuilder.and(boardData.num1.eq(num1));
        }

        // 공지글 검색
        if (notice != null) {
            andBuilder.and(boardData.notice.eq(notice));
        }

        /**
         * 조건 검색 처리
         *
         * sopt - ALL : 통합검색(제목 + 내용 + 글작성자(작성자, 회원명))
         *       SUBJECT : 제목검색
         *       CONTENT : 내용검색
         *       SUBJECT_CONTENT: 제목 + 내용 검색
         *       NAME : 이름(작성자, 회원명)
         */
        sopt = sopt != null && StringUtils.hasText(sopt.trim()) ? sopt.trim() : "ALL";
        if (skey != null && StringUtils.hasText(skey.trim())) {
            skey = skey.trim();
            BooleanExpression condition = null;

            BooleanBuilder orBuilder = new BooleanBuilder();

            /* 이름 검색 S */
            BooleanBuilder nameCondition = new BooleanBuilder();
            nameCondition.or(boardData.poster.contains(skey));
            if (boardData.member != null) {
                nameCondition.or(boardData.member.userName.contains(skey));
            }
            /* 이름 검색 E */

            if (sopt.equals("ALL")) { // 통합 검색
                orBuilder.or(boardData.subject.concat(boardData.content)
                                .contains(skey))
                        .or(nameCondition);

            } else if (sopt.equals("SUBJECT")) { // 제목 검색
                condition = boardData.subject.contains(skey);
            } else if (sopt.equals("CONTENT")) { // 내용 검색
                condition = boardData.content.contains(skey);
            } else if (sopt.contains("SUBJECT_CONTENT")) { // 제목 + 내용 검색
                condition = boardData.subject.concat(boardData.content)
                        .contains(skey);
            } else if (sopt.equals("NAME")) {
                andBuilder.and(nameCondition);
            }

            if (condition != null) andBuilder.and(condition);
            andBuilder.and(orBuilder);
        }

        /* 검색 처리 E */

        /* 정렬 처리 S */
        String sort = search.getSort();

        PathBuilder<BoardData> pathBuilder = new PathBuilder<>(BoardData.class, "boardData");
        OrderSpecifier orderSpecifier = null;
        Order order = Order.DESC;
        if (sort != null && StringUtils.hasText(sort.trim())) {
            // 정렬항목_방향   예) viewCount_DESC -> 조회수가 많은 순으로 정렬
            String[] _sort = sort.split("_");
            if (_sort[1].toUpperCase().equals("ASC")) {
                order = Order.ASC;
            }

            orderSpecifier = new OrderSpecifier(order, pathBuilder.get(_sort[0]));
        }

        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        orderSpecifiers.add(boardData.notice.desc());
        if (orderSpecifier != null) {
            orderSpecifiers.add(orderSpecifier);
        }
        orderSpecifiers.add(boardData.createdAt.desc());
        /* 정렬 처리 E */

        /* 목록 조회 처리 S */
        List<BoardData> items = queryFactory
                .selectFrom(boardData)
                .leftJoin(boardData.board)
                .fetchJoin()
                .leftJoin(boardData.member)
                .fetchJoin()
                .where(andBuilder)
                .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
                .offset(offset)
                .limit(limit)
                .fetch();

        // 추가 정보 처리
        items.forEach(this::addInfo);

        /* 목록 조회 처리 E */

        // 전체 게시글 갯수
        long total = repository.count(andBuilder);

        // 페이징 처리
        int ranges = utils.isMobile() ? board.getPageCountMobile() : board.getPageCountPc();

        Pagination pagination = new Pagination(page, (int) total, ranges, limit, request);

        return new ListData<>(items, pagination);
    }



    /**
     * 게시판 별 목록
     *
     * @param bid
     * @param search
     * @return
     */

    public ListData<BoardData> getList(String bid, BoardDataSearch search, DeleteStatus status) {
        search.setBid(bid);

        return getList(search, status);
    }

    public ListData<BoardData> getList(String bid, BoardDataSearch search) {
        return getList(bid, search, DeleteStatus.UNDELETED);
    }

    /**
     * 게시판 개별 조회
     *
     * @param seq
     * @return
     */
    public BoardData get(Long seq, DeleteStatus status) {

        BooleanBuilder andBuilder = new BooleanBuilder();
        QBoardData boardData = QBoardData.boardData;
        andBuilder.and(boardData.seq.eq(seq));

        // 삭제, 미삭제 게시글 조회 처리
        if (status != DeleteStatus.ALL) {
            if (status == DeleteStatus.UNDELETED) {
                andBuilder.and(boardData.deletedAt.isNull()); // 미삭된 게시글
            } else {
                andBuilder.and(boardData.deletedAt.isNotNull()); // 삭제된 게시글
            }
        }

        BoardData item = queryFactory.selectFrom(boardData)
                .leftJoin(boardData.board)
                .fetchJoin()
                .leftJoin(boardData.member)
                .fetchJoin()
                .where(andBuilder)
                .fetchFirst();

        if (item == null) {
            throw new BoardDataNotFoundException();
        }

        // 추가 데이터 처리
        addInfo(item);

        // 댓글 목록
        List<CommentData> comments = commentInfoService.getList(seq);
        item.setComments(comments);
        System.out.println("item : " + item);

        return item;
    }

    public BoardData get(Long seq) {
        return get(seq, DeleteStatus.UNDELETED);
    }

    /**
     * BoardData 엔티티 -> RequestBoard 커맨드 객체로 변환
     *
     * @param seq
     * @return
     */
    public RequestBoard getForm(Long seq, DeleteStatus status) {
        BoardData item = get(seq, status);

        return getForm(item);
    }

    public RequestBoard getForm(BoardData item) {

        RequestBoard form = modelMapper.map(item, RequestBoard.class);
        form.setBid(item.getBoard().getBid());

        form.setGuest(item.getMember() == null);

        return form;
    }

    public RequestBoard getForm(Long seq) {
        return getForm(seq, DeleteStatus.UNDELETED);
    }

    /**
     * 내가 작성한 게시글 목록
     */
    public ListData<BoardData> getMyList(CommonSearch search) {
        if (!memberUtil.isLogin()) {
            return new ListData<>();
        }

        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 10 : limit;
        int offset = (page - 1) * limit;


        QBoardData boardData = QBoardData.boardData;
        BooleanBuilder andBuilder = new BooleanBuilder();
        andBuilder.and(boardData.member.seq.eq(memberUtil.getMember().getSeq()));

        List<BoardData> items = queryFactory.selectFrom(boardData)
                .where(andBuilder)
                .leftJoin(boardData.board)
                .fetchJoin()
                .offset(offset)
                .limit(limit)
                .orderBy(boardData.createdAt.desc())
                .fetch();

        long total = repository.count(andBuilder);
        int ranges = utils.isMobile() ? 5 : 10;
        Pagination pagination = new Pagination(page, (int) total, ranges, limit, request);


        return new ListData<>(items, pagination);
    }

    /**
     * 내가 작성한 댓글 목록
     */
    public ListData<CommentData> getMyComment(CommonSearch search) {
        if (!memberUtil.isLogin()) {
            return new ListData<>();
        }

        int page = Math.max(search.getPage(), 1);
        int limit = Math.max(search.getLimit(), 10);
        int offset = (page - 1) * limit;

        QCommentData commentData = QCommentData.commentData;
        BooleanBuilder andBuilder = new BooleanBuilder();
        andBuilder.and(commentData.member.seq.eq(memberUtil.getMember().getSeq()));


        List<CommentData> items = queryFactory.selectFrom(commentData)
                .where(andBuilder)
                .leftJoin(commentData.boardData)
                .fetchJoin()
                .offset(offset)
                .limit(limit)
                .orderBy(commentData.createdAt.desc())
                .fetch();

        int total = queryFactory.selectFrom(commentData)
                .where(andBuilder)
                .fetch().size();
        int ranges = utils.isMobile() ? 5 : 10;
        Pagination pagination = new Pagination(page, total, ranges, limit, request);


        return new ListData<>(items, pagination);
    }

    /**
     * 내가 찜한 게시글 목록
     *
     * @param search
     * @return
     */
    public ListData<BoardData> getWishList(CommonSearch search) {

        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 10 : limit;
        int offset = (page - 1) * limit;


        List<Long> seqs = wishListService.getList(WishType.BOARD);
        if (seqs == null || seqs.isEmpty()) {
            return new ListData<>();
        }

        QBoardData boardData = QBoardData.boardData;
        BooleanBuilder andBuilder = new BooleanBuilder();
        andBuilder.and(boardData.seq.in(seqs));

        List<BoardData> items = queryFactory.selectFrom(boardData)
                .where(andBuilder)
                .leftJoin(boardData.board)
                .fetchJoin()
                .offset(offset)
                .limit(limit)
                .orderBy(boardData.createdAt.desc())
                .fetch();

        long total = repository.count(andBuilder);
        int ranges = utils.isMobile() ? 5 : 10;
        Pagination pagination = new Pagination(page, (int) total, ranges, limit, request);

        return new ListData<>(items, pagination);
    }

    /**
     * 추가 데이터 처리
     * - 업로드한 파일 목록
     * 에디터 이미지 목록, 첨부 파일 이미지 목록
     * - 권한 : 글쓰기, 글수정, 글 삭제, 글 조회 가능 여부
     * - 댓글 ..
     *
     * @param item
     */
    public void addInfo(BoardData item) {

        // 업로드한 파일 목록 S
        String gid = item.getGid();
        List<FileInfo> editorImages = fileInfoService.getList(gid, "editor");
        List<FileInfo> attachFiles = fileInfoService.getList(gid, "attach");

        List<FileInfo> selectedImages = fileInfoService.getSelectedImages(gid);

        item.setEditorImages(editorImages);
        item.setAttachFiles(attachFiles);
        item.setSelectedImages(selectedImages);
        // 업로드한 파일 목록 E

        /* 게시글 권한 정보 처리 S */
        boolean editable = false, commentable = false, mine = false;

        // 관리자는 모든 권한 가능
        if (memberUtil.isAdmin()) {
            editable = commentable = true;
        }

        // 회원 - 직접 작성한 게시글인 경우만 수정,삭제(editable)
        Member boardMember = item.getMember(); // 게시글을 작성한 회원
        Member loggedMember = memberUtil.getMember(); // 로그인한 회원

        if (boardMember != null && memberUtil.isLogin() && boardMember.getEmail().equals(loggedMember.getEmail())) {
            editable = true; // 수정, 삭제 가능
            mine = true; // 게시글 소유자
        }

        // 비회원 - 비회원 비밀번호를 검증한 경우 - 게시글 소유자, 수정, 삭제 가능
        // 비회원이 비밀번호를 검증한 경우 세션 키 : confirmed_board_data_게시글번호, 값 true
        HttpSession session = request.getSession();
        Boolean guestConfirmed = (Boolean) session.getAttribute("confirm_board_data_" + item.getSeq());
        if (boardMember == null && guestConfirmed != null && guestConfirmed) { // 비회원 비밀번호가 인증된 경우
            editable = true;
            mine = true;
        }

        // 댓글 작성 가능 여부 - 전체 : 모두 가능(비회원 + 회원 + 관리자), 회원 + 관리자 , 관리자
        Board board = item.getBoard();
        Authority authority = board.getCommentAccessType();
        if (authority == Authority.ALL || memberUtil.isAdmin()) {
            commentable = true;
        }

        if (authority == Authority.USER && memberUtil.isLogin()) {
            commentable = true;
        }

        item.setEditable(editable);
        item.setCommentable(commentable);
        item.setMine(mine);

        /* 게시글 권한 정보 처리 E */

        // 게시글 버튼 노출 권한 처리 S
        boolean showEdit = false, showWrite = false, showList= false, showDelete = false;

        Authority editAuthority = board.getWriteAccessType(); // 글작성, 수정 권한
        Authority listAuthority = board.getListAccessType(); // 글목록 보기 권한


        if (editAuthority == Authority.ALL || boardMember == null ||
                (editAuthority == Authority.USER && memberUtil.isLogin())) { // 수정 삭제 권한이 ALL인 경우, 비회원인 경우, 회원만 가능한 경우 + 로그인한 경우 수정, 삭제 버튼 클릭시 비회원 검증 하므로 노출
            showEdit = showDelete = true;
        }

        if (listAuthority == Authority.ALL || (listAuthority == Authority.USER && memberUtil.isLogin())) {
            showList = true;
        }

        if (memberUtil.isAdmin()) { // 관리자는 모든 권한 가능
            showWrite = showEdit = showDelete = showList = true;
        }

        if (boardMember == null || mine) { // 비회원 게시글이거나 회원게시글의 소유자 인 경우
            showEdit = showDelete = true;
        }

        item.setShowWrite(showWrite);

        item.setShowEdit(showEdit);
        item.setShowDelete(showDelete);
        item.setShowList(showList);
        // 게시글 버튼 노출 권한 처리 E
    }




}