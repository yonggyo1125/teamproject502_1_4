package org.g9project4.board.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.g9project4.board.controllers.BoardDataSearch;
import org.g9project4.board.entities.BoardData;
import org.g9project4.board.entities.QBoardData;
import org.g9project4.board.repositories.BoardDataRepository;
import org.g9project4.global.ListData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardInfoService {

    private final JPAQueryFactory queryFactory;
    private final BoardDataRepository repository;

    /**
     * 게시글 목록 조회
     *
     * @return
     */
    public ListData<BoardData> getList(BoardDataSearch search) {

        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();

        String sopt = search.getSopt();
        String skey = search.getSkey();

        String bid = search.getBid();
        List<String> bids = search.getBids(); // 게시판 여러개 조회

        /* 검색 처리 S */
        QBoardData boardData = QBoardData.boardData;
        BooleanBuilder andBuilder = new BooleanBuilder();

        if (bid != null && StringUtils.hasText(bid.trim())) { // 게시판별 조회
            andBuilder.and(boardData.board.bid.eq(bid.trim()));

        } else if (bids != null && !bids.isEmpty()){ // 게시판 여러개 조회
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

        }

        /* 검색 처리 E */

        return null;
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

        return null;
    }
}
