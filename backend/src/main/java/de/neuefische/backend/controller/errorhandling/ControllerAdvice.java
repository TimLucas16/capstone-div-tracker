package de.neuefische.backend.controller.errorhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
@Slf4j
@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleConflict(IllegalArgumentException ex, WebRequest request) {

        ErrorResponse bodyOfResponse = ErrorResponse.builder()
                .error(HttpStatus.BAD_REQUEST)
                .errorMessage(ex.toString())
                .timestamp(LocalDateTime.now())
                .requestUri(request.getDescription(false))
                .build();

        log.error("There was an illegal argument: " + ex);

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

        log.error("There was a NoSuchElementException: " + ex);

        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { ApiNoResponseException.class })
    protected ResponseEntity<Object> handleConflict(ApiNoResponseException ex, WebRequest request) {

        ErrorResponse bodyOfResponse = ErrorResponse.builder()
                .error(HttpStatus.BAD_GATEWAY)
                .errorMessage(ex.toString())
                .timestamp(LocalDateTime.now())
                .requestUri(request.getDescription(false))
                .build();

        log.error("There was a InvalidApiResponseException: " + ex);

        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_GATEWAY, request);
    }

    @ExceptionHandler(value = { InvalidApiKeyException.class })
    protected ResponseEntity<Object> handleConflict(InvalidApiKeyException ex, WebRequest request) {

        ErrorResponse bodyOfResponse = ErrorResponse.builder()
                .error(HttpStatus.BAD_REQUEST)
                .errorMessage(ex.toString())
                .timestamp(LocalDateTime.now())
                .requestUri(request.getDescription(false))
                .build();

        log.error("There was a InvalidApiResponseException: " + ex);

        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}