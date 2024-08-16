package org.g9project4.board.services;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.g9project4.board.entities.BoardData;
import org.g9project4.board.repositories.BoardDataRepository;
import org.g9project4.global.ListData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ListData<BoardData> getList() {

        return null;
    }

    /**
     * 게시글 개별 조회
     *
     * @param seq
     * @return
     */
    public BoardData get(Long seq) {

        return null;
    }
}
