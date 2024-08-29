package org.g9project4.mypage.validators;

import org.g9project4.global.validators.MobileValidator;
import org.g9project4.global.validators.PasswordValidator;
import org.g9project4.mypage.controllers.RequestProfile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProfileUpdateValidator implements Validator, PasswordValidator, MobileValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestProfile.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        if (errors.hasErrors()) { //커맨드 객체 검증 실패 시 종료
            return;
        }

        /**
         * 1.비밀번호가 입력된 경우
         *      - 비밀번호 복잡성 체크
         *      - 비밀번호 확인 일치 여부 체크
         * 2. 휴대전화 입력된 경우
         *      - 형식 체크
         */

        RequestProfile form = (RequestProfile) target;
        String password = form.getPassword();
        String confirmPassword = form.getConfirmPassword();
        String mobile = form.getMobile();

        //1.비밀번호가 입력된 경우
        if (StringUtils.hasText(password)) {
            if (password.length() < 8) {
                errors.rejectValue("password", "Size");
            }

            if (!password.equals(confirmPassword)) {
                errors.rejectValue("confirmPassword", "MisMatch.password");
            }

            if (!alphaCheck(password, false) || !numberCheck(password) || !specialCharsCheck(password)) {
                errors.rejectValue("password", "Complexity");
            }

            //2. 휴대전화 입력된 경우
            if (StringUtils.hasText(mobile) && !mobileCheck(mobile)) {
                errors.rejectValue("mobile", "Mobile");
            }
        }
    }
}
