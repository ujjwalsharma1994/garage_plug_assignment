package com.java.assignment.controller;

import com.java.assignment.exceptions.DetailsNotProvidedException;
import com.java.assignment.exceptions.OrderNotFoundException;
import com.java.assignment.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DetailsNotProvidedException.class)
    public ResponseEntity<Object> handleDetailsNotProvidedException(DetailsNotProvidedException ex,
                                                                    WebRequest wr) {
        return createResponseBody(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex,
                                                                    WebRequest wr) {
        return createResponseBody(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex,
                                                                 WebRequest wr) {
        return createResponseBody(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Object> handleOrderNotFoundException(OrderNotFoundException ex,
                                                               WebRequest wr) {
        return createResponseBody(ex, HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<Object> createResponseBody(Exception ex, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
