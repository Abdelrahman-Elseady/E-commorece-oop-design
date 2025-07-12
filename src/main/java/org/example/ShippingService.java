package org.example;

import java.util.List;

class ShippingService {
    private double FeePerKilo = 10.0;

    public double calculateShippingCost(List<CartItem> shippableItems) {
        return shippableItems.stream()
                .mapToDouble(item -> {
                    IShippable shippable = (IShippable) item.getProduct();
                    return shippable.getWeight() * item.getQuantity() * FeePerKilo;
                })
                .sum();
    }

    public void printShippingNotice(List<CartItem> shippableItems) {
        System.out.println("\n*** Shipping Notice ***");
        shippableItems.forEach(item -> {
            IShippable shippable = (IShippable) item.getProduct();
            double totalWeight = shippable.getWeight() * item.getQuantity();
            System.out.println(item.getQuantity() + " x " + item.getProduct().getName() + " (" + String.format("%.2f", totalWeight) + " kg)");

        });

        double totalWeight = shippableItems.stream()
                .mapToDouble(item -> {
                    IShippable shippable = (IShippable) item.getProduct();
                    return shippable.getWeight() * item.getQuantity();
                })
                .sum();
        System.out.printf("TOTAL WEIGHT: %.2f kg\n", totalWeight);
    }
}
