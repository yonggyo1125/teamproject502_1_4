package org.g9project4.file.exceptions;


import org.g9project4.global.exceptions.script.AlertBackException;
import org.springframework.http.HttpStatus;

public class FileNotFoundException extends AlertBackException {
    public FileNotFoundException() {
        super("NotFound.file",HttpStatus.NOT_FOUND);
        setErrorCode(true);
    }
}
