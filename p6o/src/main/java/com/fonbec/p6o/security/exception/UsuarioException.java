package com.fonbec.p6o.security.exception;

import org.springframework.http.HttpStatus;

public class UsuarioException extends RuntimeException {

    final HttpStatus status;

    public UsuarioException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        
    }

    public UsuarioException(HttpStatus status ,String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public UsuarioException(HttpStatus status,Throwable cause) {
        super(cause);
        this.status = status;
    }
    
}
