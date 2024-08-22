package org.hidog.member.exceptions;

import org.hidog.global.exceptions.script.AlertBackException;
import org.springframework.http.HttpStatus;

public class DuplicateMemberException extends AlertBackException {

    public DuplicateMemberException() {
        super("Duplicated.member", HttpStatus.BAD_REQUEST);
        setErrorCode(true);
    }
}