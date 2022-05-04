package com.github.lehasoldat.restaurant_voting.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class AppException extends ResponseStatusException {

    public AppException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
