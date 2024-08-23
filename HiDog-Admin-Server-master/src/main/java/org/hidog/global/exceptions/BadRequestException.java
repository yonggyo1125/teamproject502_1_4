package org.hidog.global.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public class BadRequestException extends CommonException {
    public BadRequestException(Map<String, List<String>> errorMessages) {
        super("", HttpStatus.BAD_REQUEST);
        setErrorMessages(errorMessages);
    }

    public BadRequestException(String errorCode) {
        super(errorCode, HttpStatus.BAD_REQUEST);
        setErrorCode(true);
    }
}
