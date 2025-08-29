package com.techm.mobicom.exception;

@SuppressWarnings("serial")
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}