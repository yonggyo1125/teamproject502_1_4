package org.g9project4.member.exceptions;

import org.g9project4.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends CommonException {
    public MemberNotFoundException() {
        super("NotFound.member", HttpStatus.NOT_FOUND);
        setErrorCode(true);
    }
}
