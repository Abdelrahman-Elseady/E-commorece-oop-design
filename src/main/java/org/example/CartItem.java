package org.example;

class CartItem {
    private final IProduct product;
    private int quantity;

    public CartItem(IProduct product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public IProduct getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public void increaseQuantity(int amount) { quantity += amount; }
    public void decreaseQuantity(int amount) {
        if (amount > quantity) {
            throw new IllegalArgumentException("Cannot reduce quantity below zero");
        }
        quantity -= amount;
    }
    public double getItemTotal() { return product.getPrice() * quantity; }
}
