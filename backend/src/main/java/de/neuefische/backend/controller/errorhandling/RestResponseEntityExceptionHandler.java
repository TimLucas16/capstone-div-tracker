package de.neuefische.backend.controller.errorhandling;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleConflict(IllegalArgumentException ex, WebRequest request) {

        ErrorResponse bodyOfResponse = ErrorResponse.builder()
                .error(HttpStatus.BAD_REQUEST)
                .errorMessage(ex.toString())
                .timestamp(LocalDateTime.now())
                .requestUri(request.getDescription(false))
                .build();
        return handleExceptionInternal(ex, bodyOfResponse, 
          new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { NoSuchElementException.class })
    protected ResponseEntity<Object> handleConflict(NoSuchElementException ex, WebRequest request) {

        ErrorResponse bodyOfResponse = ErrorResponse.builder()
                .error(HttpStatus.NOT_FOUND)
                .errorMessage(ex.toString())
                .timestamp(LocalDateTime.now())
                .requestUri(request.getDescription(false))
                .build();

        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { ApiNotResponseException.class })
    protected ResponseEntity<Object> handleConflict(ApiNotResponseException ex, WebRequest request) {

        ErrorResponse bodyOfResponse = ErrorResponse.builder()
                .error(HttpStatus.BAD_GATEWAY)
                .errorMessage(ex.toString())
                .timestamp(LocalDateTime.now())
                .requestUri(request.getDescription(false))
                .build();

        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_GATEWAY, request);
    }

    @ExceptionHandler(value = { InvalidApiResponseException.class })
    protected ResponseEntity<Object> handleConflict(InvalidApiResponseException ex, WebRequest request) {

        ErrorResponse bodyOfResponse = ErrorResponse.builder()
                .error(HttpStatus.BAD_GATEWAY)
                .errorMessage(ex.toString())
                .timestamp(LocalDateTime.now())
                .requestUri(request.getDescription(false))
                .build();

        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_GATEWAY, request);
    }
}