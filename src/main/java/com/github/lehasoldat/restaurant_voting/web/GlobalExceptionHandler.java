package com.github.lehasoldat.restaurant_voting.web;

import com.github.lehasoldat.restaurant_voting.error.AppException;
import com.github.lehasoldat.restaurant_voting.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    ErrorAttributes errorAttributes;

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Object> handleAppException(WebRequest request, AppException exception) {
        log.error("AppException: {}", exception.getReason());
        String msg = exception.getReason();
        HttpStatus status = exception.getStatus();
        return createResponseEntity(request, ErrorAttributeOptions.of(MESSAGE), msg, status);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAccessException(WebRequest request, DataAccessException exception) {
        log.error("DataAccessException: {}", exception.getMessage());
        String msg = exception.getMessage();
        if (msg != null && msg.contains("VOTING_TIME"))
            msg = "You can not vote after 11:00";
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        return createResponseEntity(request, ErrorAttributeOptions.of(MESSAGE), msg, status);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.error("HttpMessageNotReadable: {}", exception.getMessage());
        String msg = ValidationUtil.getRootCause(exception).getMessage();
        status = HttpStatus.BAD_REQUEST;
        return createResponseEntity(request, ErrorAttributeOptions.of(MESSAGE), msg, status);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException exception, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.error("TypeMismatch: {}", exception.getMessage());
        String msg = exception.getMessage();
        if (exception.getRequiredType() != null) {
            msg = String.format("%s is not %s", exception.getValue(), exception.getRequiredType().getSimpleName());
        }
        status = HttpStatus.BAD_REQUEST;
        return createResponseEntity(request, ErrorAttributeOptions.of(MESSAGE), msg, status);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.error("MethodArgumentNotValid: {}", exception.getMessage());
        String msg = exception.getFieldErrors().stream()
                .map(fieldError -> String.format("[%s] %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining("\n"));
        status = HttpStatus.UNPROCESSABLE_ENTITY;
        return createResponseEntity(request, ErrorAttributeOptions.of(MESSAGE), msg, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(WebRequest webRequest, ConstraintViolationException exception) {
        log.error("ConstraintViolationException: {}", exception.getMessage());
        String msg = exception.getConstraintViolations().stream()
                .map(constraintViolation -> String.format("[%s] %s", constraintViolation.getPropertyPath(), constraintViolation.getMessage()))
                .collect(Collectors.joining("\n"));
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        return createResponseEntity(webRequest, ErrorAttributeOptions.of(MESSAGE), msg, status);
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

