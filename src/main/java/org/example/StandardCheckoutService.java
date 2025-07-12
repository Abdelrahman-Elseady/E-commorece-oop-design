package org.example;

class StandardCheckoutService implements ICheckoutService {
    @Override
    public void processCheckout(Cart cart, Customer customer) {
        if (cart.isEmpty()) {
            System.out.println("Cannot checkout empty cart");
            return;
        }

        try {
            double total = cart.getSubtotal();
            printReceipt(cart, total);
            customer.getAccount().withdraw(total);
            cart.finalizePurchase();
            cart.clear();
            System.out.println("\nCheckout successful!");
        } catch (Exception e) {
            System.out.println("Checkout failed: " + e.getMessage());
        }
    }

    private void printReceipt(Cart cart, double total) {
        System.out.println("\n*** Checkout Receipt ***");
        cart.getItems().forEach(item ->
                System.out.println(item.getQuantity() + " x " + item.getProduct().getName() + " $" + String.format("%.2f", item.getItemTotal())));

        System.out.println("-----------------------");
        System.out.println("TOTAL:          $" + String.format("%7.2f", total));
    }
}

