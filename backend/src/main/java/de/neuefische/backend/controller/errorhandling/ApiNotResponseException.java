package de.neuefische.backend.controller.errorhandling;

public class ApiNotResponseException extends RuntimeException{
    public ApiNotResponseException(String message) {
        super(message);
    }
}
