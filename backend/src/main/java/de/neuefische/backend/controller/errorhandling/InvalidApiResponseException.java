package de.neuefische.backend.controller.errorhandling;

public class InvalidApiResponseException extends RuntimeException{
    public InvalidApiResponseException(String message) {
        super(message);
    }
}
