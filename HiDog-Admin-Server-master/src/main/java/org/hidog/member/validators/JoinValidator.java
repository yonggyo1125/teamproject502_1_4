package org.hidog.member.validators;

import lombok.RequiredArgsConstructor;
import org.hidog.global.validators.PasswordValidator;
import org.hidog.member.controllers.RequestJoin;
import org.hidog.member.repositories.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class JoinValidator implements Validator, PasswordValidator {
    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestJoin.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }

        /**
         * 1. 이미 가입된 회원인지 체크
         * 2. 비밀번호, 비밀번호 확인 일치 여부
         * 3. 비밀번호 복잡성 체크
         * 4. 휴대전화번호 형식 체크
         */

        RequestJoin form = (RequestJoin) target;
        String email = form.getEmail();
        String password = form.getPassword();
        String confirmPassword = form.getConfirmPassword();
        String userName = form.getUserName();

        //1. 이미 가입된 회원인지 체크
        if (memberRepository.existsByEmail(email)) {
            errors.rejectValue("email", "Duplicated");
        }

        //2. 비밀번호, 비밀번호 확인 일치 여부
        if (!password.equals(confirmPassword)) {
            errors.rejectValue("confirmPassword", "Mismatch.password");
        }

        // 3. 비밀번호 복잡성 체크 - 알파벳 대소문자 각각 1개 이상, 숫자 1개 이상, 특수문자 1개 이상
        if (!alphaCheck(password, true) || !numberCheck(password) || !specialCharsCheck(password)) {
            errors.rejectValue("password", "Complexity");
        }

        //4. 닉네임 중복 체크
        if (memberRepository.existsByUserName(userName)) {
            errors.rejectValue("userName", "Duplicated");
        }
    }
}