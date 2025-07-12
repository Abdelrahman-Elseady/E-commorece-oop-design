package org.example;

class ShippingCheckoutService implements ICheckoutService {
    private final ShippingService shippingService = new ShippingService();

    @Override
    public void processCheckout(Cart cart, Customer customer) {
        if (cart.isEmpty()) {
            System.out.println("Cannot checkout empty cart");
            return;
        }

        try {
            double subtotal = cart.getSubtotal();
            double shippingCost = shippingService.calculateShippingCost(cart.getShippableItems());
            double total = subtotal + shippingCost;

            shippingService.printShippingNotice(cart.getShippableItems());
            printReceipt(cart, subtotal, shippingCost, total);
            customer.getAccount().withdraw(total);

            cart.finalizePurchase();
            cart.clear();

            System.out.println("\nCheckout successful!");
        } catch (Exception e) {
            System.out.println("Checkout failed: " + e.getMessage());
        }
    }

    private void printReceipt(Cart cart, double subtotal, double shipping, double total) {
        System.out.println("\n*** Checkout Receipt ***");
        cart.getItems().forEach(item ->
                System.out.println(item.getQuantity() + " x " + item.getProduct().getName() + " $" + String.format("%.2f", item.getItemTotal())));

        System.out.println("-----------------------");
        System.out.println("SUBTOTAL:       $" + String.format("%7.2f", subtotal));
        System.out.println("SHIPPING:       $" + String.format("%7.2f", shipping));
        System.out.println("TOTAL:          $" + String.format("%7.2f", total));

    }
}
