package org.g9project4.board.exceptions;

import org.g9project4.global.exceptions.script.AlertException;
import org.springframework.http.HttpStatus;

public class GuestPasswordMismatchException extends AlertException {
    public GuestPasswordMismatchException() {
        super("Mismatch.password", HttpStatus.BAD_REQUEST);
        setErrorCode(true);
    }
}
