package org.g9project4.publicData.tour.exceptions;

import org.g9project4.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends CommonException {
    public CategoryNotFoundException(){
        super("category.NotFound", HttpStatus.NOT_FOUND);
        setErrorCode(true);
    }
}
