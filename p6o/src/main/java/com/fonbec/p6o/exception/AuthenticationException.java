package com.fonbec.p6o.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends RuntimeException{
      final HttpStatus status;

    public AuthenticationException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        
    }

    public AuthenticationException(HttpStatus status ,String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public AuthenticationException(HttpStatus status,Throwable cause) {
        super(cause);
        this.status = status;
    }
}
