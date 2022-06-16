package de.neuefische.backend.controller.errorhandling;

public class ApiUnexpectedResponseException extends RuntimeException{
    public ApiUnexpectedResponseException(String message) {
        super(message);
    }
}
