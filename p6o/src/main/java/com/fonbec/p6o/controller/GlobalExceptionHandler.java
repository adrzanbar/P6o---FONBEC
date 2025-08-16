package com.fonbec.p6o.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fonbec.p6o.exception.AuthenticationException;
import com.fonbec.p6o.exception.BaseServiceException;
import com.fonbec.p6o.exception.UsuarioException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Object> buildResponse(HttpStatus status, String mensaje) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("status", status.value());
        error.put("error", status.getReasonPhrase());
        error.put("message", mensaje);
        error.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(error);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        error.put("timestamp", LocalDateTime.now());

        List<String> errores = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        error.put("message", "Error de validacion");
        error.put("errors", errores);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(error);
    }


    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Object> handleAuthorizationException(AuthorizationDeniedException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Error de autorizacion: " + ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Error de autenticacion: " + ex.getMessage());
    }


    @ExceptionHandler(BaseServiceException.class)
    public ResponseEntity<Object> handleBaseServiceException(BaseServiceException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Error de servicio: " + ex.getMessage());
    }

    @ExceptionHandler(UsuarioException.class)
    public ResponseEntity<Object> handleUsuarioException(UsuarioException ex) {
        return buildResponse(ex.getStatus(), "Error de usuario: " + ex.getMessage());
    }

    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno: " + ex.getMessage());
    }
}