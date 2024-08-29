package org.g9project4.publicData.tour.exceptions;

import org.g9project4.global.exceptions.script.AlertBackException;
import org.springframework.http.HttpStatus;

public class DetailNotFoundException extends AlertBackException {
    public DetailNotFoundException() {
        super("NotFound.detail", HttpStatus.NOT_FOUND);
    }
}
