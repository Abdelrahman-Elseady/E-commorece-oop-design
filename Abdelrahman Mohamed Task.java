package org.example;

import java.util.*;

class Product {
    protected String name;
    protected double price;
    protected int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public int getQuantity() { return quantity; }

    public void increaseQuantity(int amount) { quantity += amount; }
    public void decreaseQuantity(int amount) { quantity -= amount; }

    public Product copy(int quantity) {
        return new Product(this.name, this.price, quantity);
    }
}

interface IShippingProduct {
    String getName();
    double getWeight();
}

interface IExpiringProduct {
    Date getExpiryDate();
    boolean isExpired();
    void setExpiryDate(Date expiryDate);
}

class Cheese extends Product implements IShippingProduct {
    private double weightPerUnit;
    public Cheese(String name, double price, int quantity, double weightPerUnit) {
        super(name, price, quantity);
        this.weightPerUnit = weightPerUnit;
    }
    public double getWeight() { return weightPerUnit; }

    @Override
    public Product copy(int quantity) {
        return new Cheese(this.name, this.price, quantity, this.weightPerUnit);
    }
}

class Orange extends Product implements IShippingProduct, IExpiringProduct {
    private double weightPerUnit;
    private Date expiryDate;
    public Orange(String name, double price, int quantity, double weightPerUnit, Date expiryDate) {
        super(name, price, quantity);
        this.weightPerUnit = weightPerUnit;
        this.expiryDate = expiryDate;
    }
    public double getWeight() { return weightPerUnit; }
    public Date getExpiryDate() { return expiryDate; }
    public boolean isExpired() { return expiryDate.before(new Date()); }
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public Product copy(int quantity) {
        return new Orange(this.name, this.price, quantity, this.weightPerUnit, this.expiryDate);
    }
}

class Biscuits extends Product implements IShippingProduct {
    private double weightPerUnit;
    public Biscuits(String name, double price, int quantity, double weightPerUnit) {
        super(name, price, quantity);
        this.weightPerUnit = weightPerUnit;
    }
    public double getWeight() { return weightPerUnit; }

    @Override
    public Product copy(int quantity) {
        return new Biscuits(this.name, this.price, quantity, this.weightPerUnit);
    }
}

class Tv extends Product {
    Tv(String name, double price, int quantity) {
        super(name, price, quantity);
    }

    @Override
    public Product copy(int quantity) {
        return new Tv(this.name, this.price, quantity);
    }
}

class ShippingService {
    private List<IShippingProduct> shippingProducts;
    public ShippingService(List<IShippingProduct> products) {
        this.shippingProducts = products;
    }

    public double getTotalWeight() {
        double totalWeight = 0;
        for (IShippingProduct item : shippingProducts) {
            Product p = (Product) item;
            totalWeight += item.getWeight() * p.getQuantity();
        }
        return totalWeight;
    }

    public void printShipmentNotice() {
        if (shippingProducts.isEmpty()) {
            System.out.println("No items to ship.");
            return;
        }

        System.out.println("**** Shipment notice ****");
        for (IShippingProduct item : shippingProducts) {
            Product p = (Product) item;
            double totalItemWeightInGrams = item.getWeight() * p.getQuantity() * 1000;
            System.out.println(p.getQuantity() + "x " + p.getName() + "    " + (int) totalItemWeightInGrams + "g");
        }
        System.out.println("\nTotal package weight " + getTotalWeight() + "kg\n");
    }
}

class Customer {
    private String name;
    private double balance;

    public Customer(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }
    public String getName() { return name; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public boolean isBalanceEnough(double price) {
        return balance >= price;
    }
}

class Cart {
    private Customer customer;
    private List<Product> cartItems;
    private ShippingService shippingService;
    private Map<Product, Product> originalProductsMap;

    Cart(Customer customer) {
        this.customer = customer;
        this.cartItems = new ArrayList<>();
        this.shippingService = new ShippingService(new ArrayList<>());
        this.originalProductsMap = new HashMap<>();
    }

    public boolean addProduct(Product originalProduct, int quantity) {
        if (quantity <= 0) {
            System.out.println("Error: Invalid quantity for " + originalProduct.getName());
            return false;
        }

        if (quantity > originalProduct.getQuantity()) {
            System.out.println("Error: Not enough stock for " + originalProduct.getName() + " (Available: " + originalProduct.getQuantity() + ")");
            return false;
        }

        if (originalProduct instanceof IExpiringProduct) {
            IExpiringProduct expiringProduct = (IExpiringProduct) originalProduct;
            if (expiringProduct.isExpired()) {
                System.out.println("Error: Product " + originalProduct.getName() + " is expired");
                return false;
            }
        }


        Product cartItem = originalProduct.copy(quantity);
        cartItems.add(cartItem);
        originalProductsMap.put(cartItem, originalProduct);

        if (cartItem instanceof IShippingProduct) {
            shippingService = new ShippingService(getShippableItems());
        }
        return true;
    }

    public List<Product> getCartItems() {
        return cartItems;
    }

    public List<IShippingProduct> getShippableItems() {
        List<IShippingProduct> shippable = new ArrayList<>();
        for (Product p : cartItems) {
            if (p instanceof IShippingProduct) {
                shippable.add((IShippingProduct) p);
            }
        }
        return shippable;
    }

    public void printShipmentNotice() {
        shippingService.printShipmentNotice();
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    public void clear() {
        cartItems.clear();
        shippingService = new ShippingService(new ArrayList<>());
        originalProductsMap.clear();
    }

    public Product getOriginalProduct(Product cartItem) {
        return originalProductsMap.get(cartItem);
    }
}

class CheckOutService {
    private double shippingFeePerKg = 10.0;

    public double calculateShippingFee(List<IShippingProduct> shippableItems) {
        double totalWeight = 0.0;
        for (IShippingProduct item : shippableItems) {
            Product p = (Product) item;
            totalWeight += item.getWeight() * p.getQuantity();
        }
        return totalWeight * shippingFeePerKg;
    }

    public void printReceipt(Cart cart, double subtotal, double shippingFee, double total) {
        System.out.println("** Checkout receipt **");

        List<IShippingProduct> shippable = cart.getShippableItems();

        for (IShippingProduct item : shippable) {
            Product p = (Product) item;
            double itemTotal = p.getPrice() * p.getQuantity();
            System.out.println(p.getQuantity() + "x " + p.getName() + "           " + (int)itemTotal);
        }

        System.out.println("----------------------");
        System.out.println("Subtotal         " + (int)subtotal);
        System.out.println("Shipping         " + (int)shippingFee);
        System.out.println("Total           " + (int)total);
    }

    public void processCheckout(Cart cart, Customer customer) {
        if (cart.isEmpty()) {
            System.out.println("Error: Cannot checkout with an empty cart");
            return;
        }

        for (Product cartItem : cart.getCartItems()) {
            if (cartItem instanceof IExpiringProduct) {
                IExpiringProduct exp = (IExpiringProduct) cartItem;
                if (exp.isExpired()) {
                    System.out.println("Error: Cannot checkout. Product '" + cartItem.getName() + "' is expired.");
                    return;
                }
            }
        }

        double subtotal = 0.0;
        for (Product cartItem : cart.getCartItems()) {
            subtotal += cartItem.getPrice() * cartItem.getQuantity();
        }

        double shippingFee = calculateShippingFee(cart.getShippableItems());
        double total = subtotal + shippingFee;

        if (!customer.isBalanceEnough(total)) {
            System.out.printf("Error: Insufficient balance. Needed: %.2f, Available: %.2f%n",
                    total, customer.getBalance());
            return;
        }

        printReceipt(cart, subtotal, shippingFee, total);

        for (Product cartItem : cart.getCartItems()) {
            Product originalProduct = cart.getOriginalProduct(cartItem);
            if (originalProduct != null) {
                originalProduct.decreaseQuantity(cartItem.getQuantity());
            }
        }

        customer.setBalance(customer.getBalance() - total);

        cart.clear();

        System.out.printf("Checkout successful. Remaining balance: %.2f%n", customer.getBalance());
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        Customer customer = new Customer("Abdo", 40000);

        Cheese cheese = new Cheese("Cheese", 100, 5, 0.4);
        Biscuits biscuits = new Biscuits("Biscuits", 150, 5, 0.7);
        Orange orange = new Orange("Orange", 80, 5, 0.5, new Date("2026/02/02"));
        Tv tv = new Tv("TV", 600, 5);

        // Test 1: Normal checkout
        System.out.println("\n=== Test 1: Normal Checkout ===");
        Cart cart = new Cart(customer);
        cart.addProduct(cheese, 2);
        cart.addProduct(biscuits, 1);
        cart.addProduct(orange, 2);
        cart.addProduct(tv, 1);

        cart.printShipmentNotice();

        CheckOutService checkout = new CheckOutService();
        checkout.processCheckout(cart, customer);


        // Test 2: Empty cart
        System.out.println("\n=== Test 2: Empty Cart ===");
        Cart emptyCart = new Cart(customer);
        checkout.processCheckout(emptyCart, customer);

        // Test 3: Insufficient balance
        System.out.println("\n=== Test 3: Insufficient Balance ===");
        Customer poorCustomer = new Customer("Aaa", 10);
        Cart poorCart = new Cart(poorCustomer);
        poorCart.addProduct(cheese, 1);
        checkout.processCheckout(poorCart, poorCustomer);

        // Test 4: Expired product (using expired date)
        System.out.println("\n=== Test 4: Expired Product ===");
        Cart expiredCart = new Cart(customer);
        Orange expiredOrange = new Orange("Expired Orange", 80, 5, 0.5, new Date("2020/01/01"));
        expiredCart.addProduct(expiredOrange, 1);
        checkout.processCheckout(expiredCart, customer);

        // Test 5: Out of stock
        System.out.println("\n=== Test 5: Out of Stock ===");
        Cart stockCart = new Cart(customer);
        stockCart.addProduct(cheese, 10); // Only 5 available (but 2 were already sold)
        checkout.processCheckout(stockCart, customer);
    }
}