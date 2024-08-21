package org.hidog.global.exceptions.script;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

@Getter
public class AlertBackException extends AlertException {

    private String target;

    public AlertBackException(String message, HttpStatus status, String target) {
        super(message, status);

        target = StringUtils.hasText(target) ? target : "self";

        this.target = target;

    }

    public AlertBackException(String message, HttpStatus status) {
        this(message, status, null);
    }
}