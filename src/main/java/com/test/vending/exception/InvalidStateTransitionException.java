package com.test.vending.exception;

public class InvalidStateTransitionException extends RuntimeException {

    public InvalidStateTransitionException() {
    }

    public InvalidStateTransitionException(String message) {
        super(message);
    }

    public InvalidStateTransitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
