package org.example;

class CheckoutServiceFactory {
    public static ICheckoutService createCheckoutService(Cart cart) {
        return cart.hasShippableItems() ? new ShippingCheckoutService() : new StandardCheckoutService();
    }
}
