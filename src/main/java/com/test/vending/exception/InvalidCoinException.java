package com.test.vending.exception;

public class InvalidCoinException extends RuntimeException {
    private int invalidCoinValue;

    public InvalidCoinException(int invalidCoinValue) {
        super("Invalid coin value " + invalidCoinValue);
        this.invalidCoinValue = invalidCoinValue;
    }

    public InvalidCoinException(int invalidCoinValue, Throwable cause) {
        super("Invalid coin value " + invalidCoinValue, cause);
        this.invalidCoinValue = invalidCoinValue;
    }

    public int getInvalidCoinValue() {
        return invalidCoinValue;
    }
}
