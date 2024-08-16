package org.g9project4.board.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.g9project4.board.controllers.BoardDataSearch;
import org.g9project4.board.controllers.RequestBoard;
import org.g9project4.board.entities.BoardData;
import org.g9project4.board.entities.QBoardData;
import org.g9project4.board.exceptions.BoardDataNotFoundException;
import org.g9project4.board.repositories.BoardDataRepository;
import org.g9project4.global.ListData;
import org.g9project4.global.Pagination;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardInfoService {

    private final JPAQueryFactory queryFactory;
    private final BoardDataRepository repository;
    private final HttpServletRequest request;

    /**
     * 게시글 목록 조회
     *
     * @return
     */
    public ListData<BoardData> getList(BoardDataSearch search) {

        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        int offset = (page - 1) * limit;

        String sopt = search.getSopt();
        String skey = search.getSkey();

        String bid = search.getBid();
        List<String> bids = search.getBids(); // 게시판 여러개 조회

        /* 검색 처리 S */
        QBoardData boardData = QBoardData.boardData;
        BooleanBuilder andBuilder = new BooleanBuilder();

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
        Pagination pagination = new Pagination(page, (int)total, 10, limit, request);

        return new ListData<>(items, pagination);
    }

    /**
     * 게시판 별 목록
     *
     * @param bid
     * @param search
     * @return
     */
    public ListData<BoardData> getList(String bid, BoardDataSearch search) {
        search.setBid(bid);

        return getList(search);
    }

    /**
     * 게시판 개별 조회
     * @param seq
     * @return
     */
    public BoardData get(Long seq) {

        BoardData item = repository.findById(seq).orElseThrow(BoardDataNotFoundException::new);

        // 추가 데이터 처리
        addInfo(item);

        return item;
    }

    /**
     * BoardData 엔티티 -> RequestBoard 커맨드 객체로 변환
     *
     * @param seq
     * @return
     */
    public RequestBoard getForm(Long seq) {
        BoardData item = get(seq);

        return getForm(item);
    }

    public RequestBoard getForm(BoardData item) {
       return new ModelMapper().map(item, RequestBoard.class);
    }

    /**
     *  추가 데이터 처리
     *      - 업로드한 파일 목록
     *          에디터 이미지 목록, 첨부 파일 이미지 목록
     *      - 권한 : 글쓰기, 글수정, 글 삭제, 글 조회 가능 여부
     *      - 댓글 ..
     *
     * @param item
     */
    public void addInfo(BoardData item) {

    }
}
