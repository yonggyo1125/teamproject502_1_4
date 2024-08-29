package org.g9project4.global.exceptions;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends CommonException {
    public CategoryNotFoundException(){
        super("category.NotFound", HttpStatus.NOT_FOUND);
        setErrorCode(true);
    }
}
