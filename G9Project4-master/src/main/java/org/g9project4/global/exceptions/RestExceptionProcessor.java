package org.g9project4.global.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.g9project4.global.Utils;
import org.g9project4.global.rests.JSONData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;

public interface RestExceptionProcessor {
    @ExceptionHandler({Exception.class})
    default ResponseEntity<JSONData> errorHandler(Exception e, HttpServletRequest request) {

        Object message = e.getMessage();
        Utils utils = (Utils)request.getAttribute("utils");

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500
        if (e instanceof CommonException commonException) {//커맨드 객체 오류

            status = commonException.getStatus();

            Map<String, List<String>> errorMessages = commonException.getErrorMessages();
            if (errorMessages != null) message = errorMessages;
            else {
                message = commonException.isErrorCode() ? utils.getMessage((String)message) : message;
            }
        }

        JSONData data = new JSONData();
        data.setSuccess(false);
        data.setMessage(message);
        data.setStatus(status);

        e.printStackTrace();

        return ResponseEntity.status(status).body(data);
    }
}
