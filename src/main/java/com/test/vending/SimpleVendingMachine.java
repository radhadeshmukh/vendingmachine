package com.test.vending;

import com.test.vending.VendingMachine;
import com.test.vending.VendingMachineStateManager;
import com.test.vending.vo.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class SimpleVendingMachine implements VendingMachine {

    private final Map<Integer, Slot> slots = new LinkedHashMap<>();
    private final List<Coin> vendingMachineCoins = new ArrayList<>();
    private final List<Coin> initialCoins = new ArrayList<>();
    private final List<Coin> userCoins = new ArrayList<>();
    private VendingMachineStateManager vendingStateManager = new VendingMachineStateManager();
    private int soldCount = 0;

    public void initialize() {
        loadSlot(1, 5, 25, Item.Coke);
        loadSlot(2, 5, 35, Item.Pepsi);
        loadSlot(3, 5, 45, Item.Soda);
        loadInitialCoins();
    }

    private void loadInitialCoins() {
        initialCoins.add(new Coin(10));
        initialCoins.add(new Coin(10));
        initialCoins.add(new Coin(5));
        initialCoins.add(new Coin(5));
        vendingMachineCoins.addAll(initialCoins);
    }

    public void takeUserInput() {
        while (true) {
            switch (vendingStateManager.getCurrentState()) {
                case READY:             displayAndAsk();        break;
                case ACCEPTING_COINS:   askCoin();              break;
            }
        }
    }

    private void displayAndAsk() {
        printItems();
        askCoin();
    }

    private void askCoin() {
        System.out.println("-----");
        System.out.print("Please insert coin ");
        if (!userCoins.isEmpty()) {
            System.out.print("or select item");
        }
        System.out.println("\n\tPress 1 for Penny(1) \tPress 2 for Nickel(5)");
        System.out.println(  "\tPress 3 for Dime(10) \tPress 4 for Quarter(25)");
        System.out.println(  "\tPress 5 to exit      \tPress 6 to reset");
        if (!userCoins.isEmpty()) {
            System.out.println("\tPress 7 to select item \tPress 8 to refund coins");
        }

        Scanner scanner = new Scanner(System.in);
        try {
            int i = scanner.nextInt();
            switch (i) {
                case 1: insertCoin(new Coin(1));  break;
                case 2: insertCoin(new Coin(5));  break;
                case 3: insertCoin(new Coin(10)); break;
                case 4: insertCoin(new Coin(25)); break;
                case 5: printStatsRefundAndExit();      break;
                case 6: reset(); initialize();          break;
                case 7: askItem();                      break;
                case 8: refundCoins();                  break;
            }
        } catch (InputMismatchException ime) {
            System.err.println("Please enter a valid input");
        }
    }

    private void askItem() {
        printItems();
        System.out.printf("Inserted coin value: %3d\n", getSumOfCoinsInCents(userCoins));
        System.out.println("Which item would you like?");
        Scanner scanner = new Scanner(System.in);
        try {
            int i = scanner.nextInt();
            selectItem(i);
        } catch (InputMismatchException ime) {
            System.err.println("Please enter a valid input");
        }
    }

    private void printStatsRefundAndExit() {
        System.out.printf("** Sold %d items worth %d cents\n", soldCount, (getSumOfCoinsInCents(vendingMachineCoins) - getSumOfCoinsInCents(initialCoins)));
        refundCoins();
        System.out.println("Bye");
        System.exit(0);
    }

    private void printItems() {
        System.out.println("");
        System.out.println("     VENDING MACHINE     ");
        System.out.println("**** ITEMS ****");
        Iterator<Integer> iter = slots.keySet().iterator();
        int i = 0;
        while (iter.hasNext()) {
            i++;
            Integer slotNumber = iter.next();
            Slot slot = slots.get(slotNumber);
            if (i % 4 == 0) {
                System.out.println();
            }
            System.out.printf("%2d. %-30s", slotNumber, String.format("%s (%3d¢) x [%d qty]", slot.getItem(), slot.getPriceInCents(), slot.getQuantity()));
        }
        System.out.println("\n*************************");
    }

    public void reset() {
        System.out.println("*** Resetting");
        slots.clear();
        initialCoins.clear();
        vendingMachineCoins.clear();
        userCoins.clear();
        vendingStateManager = new VendingMachineStateManager();
    }

    public void loadSlot(int slotNumber, int quantity, int priceInCents, Item item) {
        Slot slot = slots.get(slotNumber);
        if (slot == null) {
            slot = new Slot(item, priceInCents);
            slot.setQuantity(quantity);
            slots.put(slotNumber, slot);
            System.out.printf("Loading %d %s to slot number %d\n", quantity, item, slotNumber);
            calculateState(Event.LOAD_ITEM);
        } else if (slot.getPriceInCents() != priceInCents || slot.getItem().equals(item)) {
            System.out.printf("Wrong item or price - Slot Item: %s Item provided: %s, Slot Price (in cents): %d, provided: %d\n",
                    slot.getItem(), item, slot.getPriceInCents(), priceInCents);
            calculateState(Event.WRONG_ITEM);
        } else {
            slot.setQuantity(slot.getQuantity() + quantity);
            System.out.printf("Loading %d %s to slot number %d, total quantity %d\n", quantity, item, slotNumber, slot.getQuantity());
            calculateState(Event.LOAD_ITEM);
        }
    }

    public void insertCoin(Coin coin) {
        userCoins.add(coin);
        calculateState(Event.COIN_INSERTED);
        System.out.printf("Inserted coin %s\n", coin);
    }

    public Pair<List<Coin>, Item> selectItem(int slotNumber) {
        calculateState(Event.ITEM_SELECTED);

        Slot slot = slots.get(slotNumber);
        if (slot == null) {
            System.out.printf("** Wrong slot number %d selected\n", slotNumber);
            calculateState(Event.INVALID_ITEM);
            return null;
        } else {
            System.out.printf("Item selected %s, slot number %d\n", slot.getItem(), slotNumber);
        }

        int change = calculateChange(slot.getPriceInCents());
        if (change == 0) {
            return dispense(slot, new ArrayList<>());
        } else if (change > 0) {
            List<Coin> coins = getChange(change);
            if (coins == null || coins.isEmpty()) {
                System.out.println("** Insufficient change in vending machine");
                calculateState(Event.INSUFFICIENT_CHANGE_IN_MACHINE);
                return null;
            } else {
                return dispense(slot, coins);
            }
        } else {
            System.out.printf("** Insufficient money inserted: %d, cost: %d\n", getSumOfCoinsInCents(userCoins), slot.getPriceInCents());
            calculateState(Event.INSUFFICIENT_MONEY_INSERTED);
            return null;
        }
    }

    public List<Coin> refundCoins() {
        List<Coin> returnCoins = new ArrayList<>(userCoins);
        if (!userCoins.isEmpty()) {
            System.out.printf("** Returning user coins: %s\n", userCoins);
            userCoins.clear();
        } else {
            System.out.println("** No coins to return");
        }
        return returnCoins;
    }

    private Pair<List<Coin>, Item> dispense(Slot slot, List<Coin> changeCoins) {
        if (slot.getQuantity() > 0) {
            soldCount++;
            slot.setQuantity(slot.getQuantity() - 1);
            userCoins.removeAll(changeCoins);
            vendingMachineCoins.removeAll(changeCoins);
            vendingMachineCoins.addAll(userCoins);
            userCoins.clear();
            System.out.println("-----------------------------------------------");
            System.out.printf("** Dispensing Item %s. Here you go 1 %s.\n", slot.getItem(), slot.getItem());
            if (changeCoins != null && !changeCoins.isEmpty()) {
                System.out.printf("** Returning change: %s\n", changeCoins);
            }
            System.out.println("-----------------------------------------------");
            calculateState(Event.DISPENSE);
            return Pair.of(changeCoins, slot.getItem());
        } else {
            System.out.printf("** Item %s out of stock, please select another item or refund\n", slot.getItem());
            calculateState(Event.OUT_OF_STOCK);
            return null;
        }
    }

    private int calculateChange(int slotPrice) {
        int valueInCents = getSumOfCoinsInCents(userCoins);
        return valueInCents - slotPrice;
    }

    private int getSumOfCoinsInCents(List<Coin> coins) {
        int valueInCents = 0;
        for (Coin coin : coins) {
            valueInCents = valueInCents + coin.getValue();
        }
        return valueInCents;
    }

    private List<Coin> getChange(int change) {
        int remainingChange = change;
        List<Coin> changeCoins = new ArrayList<>();
        List<Coin> allCoins = new ArrayList<>(vendingMachineCoins);
        allCoins.addAll(userCoins);
        allCoins.sort(Collections.reverseOrder());
        for (Coin coin : allCoins) {
            if (coin.getValue() <= remainingChange) {
                changeCoins.add(coin);
                remainingChange = remainingChange - coin.getValue();
            }
        }

        if (getSumOfCoinsInCents(changeCoins) == change) {
            return changeCoins;
        } else {
            return null;
        }
    }

    private void calculateState(Event event) {
        vendingStateManager.onEvent(event);
    }

    public State getCurrentState() {
        return vendingStateManager.getCurrentState();
    }
}