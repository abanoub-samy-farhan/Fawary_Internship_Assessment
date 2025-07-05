package ecommerce.models;

import ecommerce.services.Cart;
import ecommerce.services.Order;

import java.util.HashMap;
import java.util.Map;

public class Customer {
    private final String firstName;
    private final String lastName;
    private final String email;
    private float balance;

    public Cart cart = new Cart();
    public Map<String, Order> orders =  new HashMap<>();

    public Customer(String firstName, String lastName, String email, float balance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.balance = balance;
    }

    public String getName(){
        return this.firstName + " " + this.lastName;
    }

    public float getBalance() {
        return this.balance;
    }

    public Order getOrder(String orderId){
        return orders.get(orderId);
    }

    public void setBalance(float balance){
        this.balance = balance;
    }

    public void viewOrderHistory(){
        System.out.println("\n** Order History **");
        for (Order order : this.orders.values()){
            System.out.println("\t -" + order.toString());
        }
    }

    public void addOrder(Order order){
        orders.put(order.getId(), order);
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + " - " + this.email;
    }
}
