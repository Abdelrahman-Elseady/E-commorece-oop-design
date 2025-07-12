package org.example;

import java.time.LocalDate;
import java.util.*;



public class Main {
    public static void main(String[] args) {
        DigitalProduct EBook = new DigitalProduct("OOP ebook", 1.5, 100);
        PhysicalProduct tv = new PhysicalProduct("TV", 15.0, 10, 4.0);
        FoodProduct cheese = new FoodProduct("Cheese", 2.99, 50, 0.4, LocalDate.now().plusDays(7));
        FoodProduct biscuits = new FoodProduct("Biscuits", 1.5, 30, 0.7, LocalDate.now().plusDays(14));
        FoodProduct expiredMilk = new FoodProduct("Milk", 1.0, 10, 1.0, LocalDate.now().minusDays(1));

        Customer customer = new Customer("Abdo", 50.0);

        System.out.println("\n=== Case 1: Normal Checkout ===");
        Cart cart1 = new Cart();
        cart1.addProduct(cheese, 2);
        cart1.addProduct(biscuits, 1);
        cart1.addProduct(EBook, 1);
        customer.checkout(cart1);

        System.out.println("\n=== Case 2: Empty Cart ===");
        Cart cart2 = new Cart();
        customer.checkout(cart2);

        System.out.println("\n=== Case 3: Expired Product ===");
        Cart cart3 = new Cart();
        try {
            cart3.addProduct(expiredMilk, 1);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n=== Case 4: Insufficient Quantity ===");
        Cart cart4 = new Cart();
        try {
            cart4.addProduct(tv, 15); // Only 10 available
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n=== Case 5: Insufficient Funds ===");
        Cart cart5 = new Cart();
        cart5.addProduct(tv, 2); // Total would be $30 but customer only has $50 left
        customer.checkout(cart5);
    }
}

