package org.g9project4.planner.exceptions;

import org.g9project4.global.exceptions.script.AlertBackException;
import org.springframework.http.HttpStatus;

public class PlannerNotFoundException extends AlertBackException {
    public PlannerNotFoundException() {
        super("NotFound.planner", HttpStatus.NOT_FOUND);
        setErrorCode(true);
    }
}
