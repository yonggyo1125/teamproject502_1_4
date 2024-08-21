package org.hidog.order.exceptions;

import org.hidog.global.exceptions.script.AlertBackException;
import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends AlertBackException {
    public OrderNotFoundException() {
        super("NotFound.order", HttpStatus.NOT_FOUND);
        setErrorCode(true);
    }
}
