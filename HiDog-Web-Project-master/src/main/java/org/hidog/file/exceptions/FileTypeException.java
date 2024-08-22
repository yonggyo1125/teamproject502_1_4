package org.hidog.file.exceptions;

import org.hidog.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class FileTypeException extends CommonException {
    public FileTypeException(HttpStatus status) {
        super("FileType", status);
        setErrorCode(true);
    }
}
