package org.g9project4.file.exceptions;

import org.g9project4.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class FileTypeException extends CommonException {
    public FileTypeException(HttpStatus status) {
        super("FileType", status);
        setErrorCode(true);
    }
}
