package de.neuefische.backend.controller.errorhandling;

public class ApiNoResponseException extends RuntimeException{
    public ApiNoResponseException(String message) {
        super(message);
    }
}
