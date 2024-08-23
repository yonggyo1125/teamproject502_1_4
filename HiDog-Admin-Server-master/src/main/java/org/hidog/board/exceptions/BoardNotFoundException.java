package org.hidog.board.exceptions;

import org.hidog.global.exceptions.script.AlertBackException;
import org.springframework.http.HttpStatus;

public class BoardNotFoundException extends AlertBackException {
    public BoardNotFoundException() {
        super("NotFound.board", HttpStatus.NOT_FOUND);
        setErrorCode(true);
    }
}
