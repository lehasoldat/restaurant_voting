package com.github.lehasoldat.restaurant_voting.web;

import com.github.lehasoldat.restaurant_voting.error.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    ErrorAttributes errorAttributes;

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Object> handleAppException(WebRequest request, AppException exception) {
        return createResponseEntity(request, ErrorAttributeOptions.of(MESSAGE), exception.getReason(), exception.getStatus());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAccessException(WebRequest request, DataAccessException exception) {
        return createResponseEntity(request, ErrorAttributeOptions.of(MESSAGE), exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private ResponseEntity<Object> createResponseEntity(WebRequest request, ErrorAttributeOptions options, String msg, HttpStatus status) {
        Map<String, Object> body = errorAttributes.getErrorAttributes(request, options);
        if (msg != null) {
            body.put("message", msg);
        }
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        return ResponseEntity.status(status).body(body);
    }
}

