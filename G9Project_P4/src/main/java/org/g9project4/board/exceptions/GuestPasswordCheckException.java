package org.g9project4.board.exceptions;

import org.g9project4.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class GuestPasswordCheckException extends CommonException {
    public GuestPasswordCheckException() {
        super("비회원 비밀번호 인증 필요", HttpStatus.UNAUTHORIZED);
    }
}
