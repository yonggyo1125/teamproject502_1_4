package org.hidog.board.services;

import org.hidog.board.controllers.RequestBoard;
import org.hidog.board.entities.Board;
import org.hidog.board.entities.BoardData;
import org.hidog.board.repositories.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//@ActiveProfiles("test")
public class BoardSaeveServiceTest {

    @Autowired
    private BoardSaveService boardSaveService;

    @Autowired
    private BoardRepository boardRepository; // 게시판 생성위해 가져옴

    private Board board;

    @BeforeEach
    void init() { // 게시판 1개 생성 // 게시판이 있어야 게시글 작성, 저장 가능
        board = new Board();
        board.setBid("1");
        board.setBName("자유게시판");
        /*
        board = Board.builder()
                .bid("freetalk")
                .bName("자유게시판")
                .gid(UUID.randomUUID().toString())
                .listAccessType(Authority.ALL)
                .writeAccessType(Authority.ALL)
                .commentAccessType(Authority.ALL)
                .viewAccessType(Authority.ALL)
                .replyAccessType(Authority.ALL)
                .locationAfterWriting("list")
                .build();
         */
        boardRepository.saveAndFlush(board);
    }

    @Test
    void saveTest() {
        // 게시글 저장
        RequestBoard form = new RequestBoard();
        form.setBid(board.getBid());
        form.setCategory("분류1");
        form.setPoster("작성자");
        form.setSubject("제목");
        form.setContent("내용");
        form.setGuestPw("123ab");
        form.setNum1(25000L);
        form.setNum2(2L);
        form.setText1("맥프로16");

        BoardData data = boardSaveService.save(form);
        System.out.println(data);

        // 게시글 수정
        form.setMode("update");
        form.setSeq(data.getSeq());

        BoardData data2 = boardSaveService.save(form);
        System.out.println(data2);
    }
}
