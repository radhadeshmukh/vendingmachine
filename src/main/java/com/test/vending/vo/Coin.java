package com.test.vending.vo;

import com.test.vending.exception.InvalidCoinException;

public class Coin implements Comparable<Coin>{
    private final int value;

    public Coin(int value) {
        if (value != 1 && value != 5 && value != 10 && value != 25) {
            throw new InvalidCoinException(value);
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Coin o) {
        return value - o.getValue();
    }

    @Override
    public String toString() {
        String prettyPrint;
        switch (value) {
            case 1:
                prettyPrint = "Penny(1)";
                break;
            case 5:
                prettyPrint = "Nickel(5)";
                break;
            case 10:
                prettyPrint = "Dime(10)";
                break;
            case 25:
                prettyPrint = "Quarter(25)";
                break;
            default:
                prettyPrint = String.format("Unknown(%d)", value);
        }
        return prettyPrint;
    }

}
