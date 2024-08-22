package org.hidog.member.exceptions;

import org.hidog.global.exceptions.script.AlertException;
import org.springframework.http.HttpStatus;

public class DuplicateMemberException extends AlertException {
    public DuplicateMemberException() {
        super("Duplicated.member", HttpStatus.BAD_REQUEST);
        setErrorCode(true);
    }
}
