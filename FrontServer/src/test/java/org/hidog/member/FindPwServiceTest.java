package org.hidog.member;

import org.hidog.member.services.FindPwService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FindPwServiceTest {

    @Autowired
    private FindPwService service;



    @Test
    @DisplayName("비밀번호 초기화 및 초기화된 메일 이메일 전송 테스트")
    void resetTest() {
        Assertions.assertDoesNotThrow(() -> service.reset("chyh202@naver.com"));
    }
}
