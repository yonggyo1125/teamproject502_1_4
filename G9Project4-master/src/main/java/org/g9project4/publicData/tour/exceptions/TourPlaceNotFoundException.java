package org.g9project4.publicData.tour.exceptions;

import org.g9project4.global.exceptions.script.AlertBackException;
import org.springframework.http.HttpStatus;

public class TourPlaceNotFoundException extends AlertBackException {
    public TourPlaceNotFoundException() {
        super("NotFound.TourPlace", HttpStatus.NOT_FOUND);
    }
}
