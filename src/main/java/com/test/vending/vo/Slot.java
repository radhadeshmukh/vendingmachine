package com.test.vending.vo;

public class Slot {
    private final Item item;
    private final int priceInCents;
    private int quantity;

    public Slot(Item item, int priceInCents) {
        this.item = item;
        this.priceInCents = priceInCents;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPriceInCents() {
        return priceInCents;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Slot{" +
                "item=" + item +
                ", priceInCents=" + priceInCents +
                ", quantity=" + quantity +
                '}';
    }
}
