package org.example;

interface IProduct {
    String getName();
    double getPrice();
    int getQuantity();
    void decreaseQuantity(int amount);
}
