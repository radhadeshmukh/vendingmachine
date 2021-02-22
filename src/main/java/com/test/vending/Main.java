package com.test.vending;

import com.test.vending.SimpleVendingMachine;

public class Main {

    public static void main(String[] args) {
        SimpleVendingMachine vendingMachine = new SimpleVendingMachine();
        vendingMachine.initialize();
        vendingMachine.takeUserInput();
    }
}
