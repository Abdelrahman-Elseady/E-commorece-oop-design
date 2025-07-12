package org.example;

class Customer {
    private final String name;
    private final Account account;

    public Customer(String name, double initialBalance) {
        this.name = name;
        this.account = new Account(initialBalance);
    }

    public String getName() {
        return name;
    }

    public Account getAccount() {
        return account;
    }
    public void checkout(Cart cart) {
        ICheckoutService checkoutService = CheckoutServiceFactory.createCheckoutService(cart);
        checkoutService.processCheckout(cart, this);
    }
}
