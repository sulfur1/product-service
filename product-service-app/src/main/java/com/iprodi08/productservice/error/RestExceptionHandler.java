package com.iprodi08.productservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> dataNotFoundExceptionHandling(Exception exception, WebRequest request) {
        return new ResponseEntity<>(
                new ErrorDetails(
                        Instant.now(),
                        exception.getMessage(),
                        request.getDescription(false)
                ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandling(Exception exception, WebRequest request) {
        return new ResponseEntity<>(
                new ErrorDetails(
                        Instant.now(),
                        exception.getMessage(),
                        request.getDescription(false)
                ), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
