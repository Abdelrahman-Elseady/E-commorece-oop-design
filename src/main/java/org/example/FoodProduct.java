package org.example;

import java.time.LocalDate;

class FoodProduct extends BaseProduct implements IShippable, IExpirable {
    private final double weight;
    private final LocalDate expiryDate;

    public FoodProduct(String name, double price, int quantity, double weight, LocalDate expiryDate) {
        super(name, price, quantity);
        this.weight = weight;
        this.expiryDate = expiryDate;
    }

    @Override public double getWeight() { return weight; }
    @Override public LocalDate getExpiryDate() { return expiryDate; }
    @Override public boolean isExpired() { return LocalDate.now().isAfter(expiryDate); }
}
