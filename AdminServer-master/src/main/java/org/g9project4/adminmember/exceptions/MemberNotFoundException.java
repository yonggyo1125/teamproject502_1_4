package org.g9project4.adminmember.exceptions;

import org.g9project4.global.exceptions.script.AlertBackException;
import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends AlertBackException {
    public MemberNotFoundException() {
        super("NotFound.Member", HttpStatus.NOT_FOUND);
        setErrorCode(true);
    }
}
