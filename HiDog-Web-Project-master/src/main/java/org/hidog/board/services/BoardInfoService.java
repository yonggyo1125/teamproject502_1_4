package org.hidog.board.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hidog.board.controllers.BoardDataSearch;
import org.hidog.board.controllers.RequestBoard;
import org.hidog.board.entities.Board;
import org.hidog.board.entities.BoardData;
import org.hidog.board.entities.QBoardData;
import org.hidog.board.exceptions.BoardDataNotFoundException;
import org.hidog.board.exceptions.BoardNotFoundException;
import org.hidog.board.repositories.BoardDataRepository;
import org.hidog.file.entities.FileInfo;
import org.hidog.file.services.FileInfoService;
import org.hidog.global.ListData;
import org.hidog.global.Pagination;
import org.hidog.global.Utils;
import org.hidog.global.constants.DeleteStatus;
import org.hidog.member.MemberUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardInfoService {

    private final JPAQueryFactory jpaQueryFactory;
    private final BoardDataRepository boardDataRepository;
    private final BoardConfigInfoService configInfoService;
    private final HttpServletRequest request;
    private final MemberUtil memberUtil;
    private final Utils utils;
    private final ModelMapper modelMapper;
    private final FileInfoService fileInfoService;


    /**
     * 게시글 목록 조회
     *
     */
    public ListData<BoardData> getList(BoardDataSearch search, DeleteStatus status) {

        String bid = search.getBid();
        List<String> bids = search.getBids(); // 게시판 여러개 조회

        // 게시판 설정 조회
        Board board = bid != null && StringUtils.hasText(bid.trim()) ? configInfoService.get(bid.trim()).orElseThrow(BoardNotFoundException::new) : new Board();

        int page =  Math.max(search.getPage(), 1); // search.getPage(), 1 을 비교해서 더 큰값을 반환
        int limit = search.getLimit();
        limit = limit > 0 ? limit : board.getRowsPerPage();

        int offset = (page - 1) * limit;

        // 삭제가 되지 않은 게시글 목록이 기본 값
        status = Objects.requireNonNullElse(status, DeleteStatus.UNDELETED);

        String sopt = search.getSort(); // 검색 옵션
        String skey = search.getSkey(); // 검색 키워드


        /* 검색 처리 S*/
        QBoardData boardData = QBoardData.boardData;
        BooleanBuilder andBuilder = new BooleanBuilder();

        // 삭제, 미삭제 게시글 조회 처리
        if (status != DeleteStatus.ALL) {
            if (status == DeleteStatus.UNDELETED) {
                andBuilder.and(boardData.deletedAt.isNull()); // 미삭제 게시글
            } else {
                andBuilder.and(boardData.deletedAt.isNotNull()); // 삭제된 게시글
            }
        }


        if (bid != null && StringUtils.hasText(bid.trim())) { // 게시판별 조회
            bids = List.of(bid);
        }

        if (bids != null && !bids.isEmpty()){ // 게시판 여러개 조회
            andBuilder.and(boardData.board.bid.in(bids));
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
        orderSpecifiers.add(boardData.notice.desc()); // 공지사항이 가장 먼저 나오기
        if (orderSpecifier != null) {
            orderSpecifiers.add(orderSpecifier);
        }
        orderSpecifiers.add(boardData.createdAt.desc());
        /* 정렬 처리 E */

        /* 목록 조회 처리 S */
        List<BoardData> items = jpaQueryFactory
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
        long total = boardDataRepository.count(andBuilder);

        // 페이징 처리
        int ranges = utils.isMobile() ? board.getPageCountMobile() : board.getPageCountPc();
        Pagination pagination = new Pagination(page, (int)total, ranges, limit, request);

        return new ListData<>(items, pagination);
    }

    /**
     * 게시판 별 목록
     * 특정 게시판의 게시글목록을 조회
     *
     * @param bid : 게시판 ID
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
     * 게시글 1개 조회(엔티티)
     *
     * @param seq : 게시글 번호
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

        BoardData item = jpaQueryFactory.selectFrom(boardData)
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

        return item;
    }

    public BoardData get(Long seq) {
        return get(seq, DeleteStatus.UNDELETED);
    }

    /**
     * BoardData(엔티티) -> RequestBoard(커맨드객체)
     * 게시글 데이터(BoardData), 게시글 번호(Long)
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
     * 게시글 추가 정보 처리, 추가 데이터 처리
     *          - 업로드한 파일 목록
     *              에디터 이미지 목록, 첨부 파일 이미지 목록
     *          - 권한 : 글쓰기, 글수정, 글 삭제, 글 조회 가능 여부
     *          - 댓글
     * @param item
     */
    public void addInfo(BoardData item) {

        /* 업로드한 파일 목록 S */
       String gid = item.getGid();
       List<FileInfo> editorImages = fileInfoService.getList(gid, "editor");
       List<FileInfo> attachFiles = fileInfoService.getList(gid, "attach");

       item.setEditorImages(editorImages);
       item.setAttachFiles(attachFiles);
       /* 업로드한 파일 목록 E */
    }
}
