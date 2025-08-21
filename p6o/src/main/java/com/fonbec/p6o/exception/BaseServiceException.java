package com.fonbec.p6o.exception;

import org.springframework.http.HttpStatus;

public class BaseServiceException extends RuntimeException {

    final HttpStatus status;

    public BaseServiceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public BaseServiceException(HttpStatus status ,String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
    
}
