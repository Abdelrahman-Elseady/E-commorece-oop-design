package org.example;

class PhysicalProduct extends BaseProduct implements IShippable {
    private final double weight;

    public PhysicalProduct(String name, double price, int quantity, double weight) {
        super(name, price, quantity);
        this.weight = weight;
    }

    @Override public double getWeight() { return weight; }
}

