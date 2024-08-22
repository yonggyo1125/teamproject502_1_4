package org.hidog.payment.exceptions;

import org.hidog.global.exceptions.script.AlertRedirectException;
import org.springframework.http.HttpStatus;

public class PaymentAuthException extends AlertRedirectException {
    public PaymentAuthException(String message, String url) {// 승일실패 시 에러
        super(message, url, HttpStatus.BAD_REQUEST);
    }
}
