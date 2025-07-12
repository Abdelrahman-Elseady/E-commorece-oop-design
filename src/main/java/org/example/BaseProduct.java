package org.example;

abstract class BaseProduct implements IProduct {
    private final String name;
    private final double price;
    private int quantity;

    protected BaseProduct(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    @Override public String getName() { return name; }
    @Override public double getPrice() { return price; }
    @Override public int getQuantity() { return quantity; }
    @Override public void decreaseQuantity(int amount) {
        if (amount > quantity) {
            throw new IllegalArgumentException("Not enough stock available");
        }
        quantity -= amount;
    }
}
