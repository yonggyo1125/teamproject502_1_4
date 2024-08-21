package org.hidog.member.services;

import lombok.RequiredArgsConstructor;
import org.hidog.email.services.EmailMessage;
import org.hidog.email.services.EmailSendService;
import org.hidog.global.Utils;
import org.hidog.member.controllers.RequestFindPw;
import org.hidog.member.entities.Member;
import org.hidog.member.exceptions.MemberNotFoundException;
import org.hidog.member.repositories.MemberRepository;
import org.hidog.member.validators.FindPwValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FindPwService {

    private final FindPwValidator validator;
    private final MemberRepository repository;
    private final EmailSendService sendService;
    private final PasswordEncoder encoder;
    private final Utils utils;


    public void process(RequestFindPw form, Errors errors) {
        validator.validate(form, errors);
        if (errors.hasErrors()) { // 유효성 검사 실패시에는 처리 중단
            return;
        }

        // 비밀번호 초기화
        reset(form.email());

    }

    public void reset(String email) {
        /* 비밀번호 초기화 S */
        Member member = repository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        String newPassword = utils.randomChars(12); // 초기화 비밀번호는 12자로 생성
        member.setPassword(encoder.encode(newPassword));

        repository.saveAndFlush(member);

        /* 비밀번호 초기화 E */
        EmailMessage emailMessage = new EmailMessage(email, Utils.getMessage("Email.password.reset", "commons"), Utils.getMessage("Email.password.reset", "commons"));
        Map<String, Object> tplData = new HashMap<>();
        tplData.put("password", newPassword);
        sendService.sendMail(emailMessage, "password_reset", tplData);
    }
}
