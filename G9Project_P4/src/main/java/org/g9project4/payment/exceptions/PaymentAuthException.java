package org.g9project4.payment.exceptions;

import org.g9project4.global.exceptions.script.AlertRedirectException;
import org.springframework.http.HttpStatus;

public class PaymentAuthException extends AlertRedirectException {
    public PaymentAuthException(String message, String url) {
        super(message, url, HttpStatus.BAD_REQUEST);
    }
}
