package org.hidog.payment.exceptions;

import org.hidog.global.exceptions.script.AlertRedirectException;
import org.springframework.http.HttpStatus;

public class PaymentApprovalException extends AlertRedirectException {
    public PaymentApprovalException(String message, String url) {
        super(message, url, HttpStatus.BAD_REQUEST);
    }
}
