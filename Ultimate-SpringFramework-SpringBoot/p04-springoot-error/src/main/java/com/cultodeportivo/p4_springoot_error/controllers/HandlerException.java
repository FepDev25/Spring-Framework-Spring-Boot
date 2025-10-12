package com.cultodeportivo.p4_springoot_error.controllers;

import java.util.Date;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.cultodeportivo.p4_springoot_error.exceptions.UserNotFoundException;
import com.cultodeportivo.p4_springoot_error.models.Error;

@RestControllerAdvice
public class HandlerException {

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<Error> divisionByZero(Exception ex) {
        return ResponseEntity.internalServerError().body(
            new Error(
                ex.getMessage(),
                "Error de división por cero",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date()
            )
        );

    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> numberFormatException(Exception ex) {
        return Map.of(
            "message", ex.getMessage(),
            "error", "Error de formato de número",
            "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "date", new Date()
        );
        
    }

    @ExceptionHandler({NullPointerException.class, HttpMessageNotWritableException.class, UserNotFoundException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> userNotFound(Exception ex) {
        return Map.of(
            "message", ex.getMessage(),
            "error", "El usuario o role no existe",
            "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "date", new Date()
        );
        
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Error> notFoundEx(Exception ex) {
        Error error = new Error();
        error.setDate(new Date());
        error.setError("Api REST no encontrada");
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

        // Importante: spring.web.resources.add-mappings=false en application.properties
    }

}
