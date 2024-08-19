package org.g9project4.order.exceptions;

import org.g9project4.global.exceptions.script.AlertBackException;
import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends AlertBackException {
    public OrderNotFoundException() {
        super("NotFound.order", HttpStatus.NOT_FOUND);
        setErrorCode(true);
    }
}
