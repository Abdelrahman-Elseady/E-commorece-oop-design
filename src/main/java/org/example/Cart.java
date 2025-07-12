package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Cart {
    private final List<CartItem> items = new ArrayList<>();

    public void addProduct(IProduct product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (quantity > product.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock available");
        }
        if (product instanceof IExpirable expirable && expirable.isExpired()) {
            throw new IllegalArgumentException("Cannot add expired product to cart");
        }

        items.stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.increaseQuantity(quantity),
                        () -> items.add(new CartItem(product, quantity))
                );
    }

    public void removeProduct(IProduct product, int quantity) {
        CartItem item = items.stream()
                .filter(i -> i.getProduct().equals(product))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not in cart"));

        if (quantity >= item.getQuantity()) {
            items.remove(item);
        } else {
            item.decreaseQuantity(quantity);
        }
    }

    public List<CartItem> getItems() { return Collections.unmodifiableList(items); }
    public List<CartItem> getShippableItems() {
        return items.stream()
                .filter(item -> item.getProduct() instanceof IShippable)
                .toList();
    }
    public double getSubtotal() {
        return items.stream()
                .mapToDouble(CartItem::getItemTotal)
                .sum();
    }
    public void clear() { items.clear(); }
    public boolean isEmpty() { return items.isEmpty(); }
    public boolean hasShippableItems() { return !getShippableItems().isEmpty(); }

    public void finalizePurchase() {
        items.forEach(item ->
                item.getProduct().decreaseQuantity(item.getQuantity()));
    }
}
