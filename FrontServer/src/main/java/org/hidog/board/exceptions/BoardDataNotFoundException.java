package org.hidog.board.exceptions;

import org.hidog.global.exceptions.script.AlertBackException;
import org.springframework.http.HttpStatus;

public class BoardDataNotFoundException extends AlertBackException {

    public BoardDataNotFoundException() { // 게시글이 없을 때
        super("NotFound.boardData", HttpStatus.NOT_FOUND);
        setErrorCode(true);
    }
}
