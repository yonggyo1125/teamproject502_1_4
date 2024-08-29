package org.g9project4.member;

import org.g9project4.board.repositories.BoardDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MyPageTest {
    @Autowired
    private BoardDataRepository boardDataRepository;

    @Test
    void test1(){
//        System.out.println(boardDataRepository.findById(1L));
    }
}
