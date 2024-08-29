package org.g9project4.member;

import org.g9project4.member.constants.Gender;
import org.g9project4.member.controllers.RequestJoin;
import org.g9project4.member.repositories.MemberRepository;
import org.g9project4.member.services.MemberSaveService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class MemberJoinTest {
    @Autowired
    private MemberSaveService saveService;

    @Test
    void test1(){
        RequestJoin form = new RequestJoin();
        for (int i=0;i<10;i++){
            form.setGid("gid"+i);
            form.setEmail("test"+i+"@gmail.com");
            form.setPassword("password");
            form.setUserName("user"+i);
            form.setMobile("01000000000");
            form.setIsForeigner(true);
            form.setBirth(LocalDate.now());
            form.setGende(Gender.FEMALE);
            saveService.save(form);
        }
    }
}
