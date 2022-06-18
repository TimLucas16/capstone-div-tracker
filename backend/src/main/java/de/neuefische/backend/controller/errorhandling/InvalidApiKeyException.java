package de.neuefische.backend.controller.errorhandling;

public class InvalidApiKeyException extends RuntimeException{
    public InvalidApiKeyException(String message) {
        super(message);
    }
}
