package ecommerce.services;

import ecommerce.models.Shippable;

import java.time.LocalDate;
import java.util.ArrayList;

public class Order {
    private final String id;
    private final ArrayList<CartItem> items;
    private final float totalPrice;
    private final float shippingFees;
    private final boolean shippable;
    private OrderStatus status;
    private final LocalDate orderedAt;
    public static int orderCount = 0;

    public Order(String id, ArrayList<CartItem> items, float totalPrice, float shippingFees,
                 boolean shippable, OrderStatus status, LocalDate orderedAt) {
        this.id = id;
        this.items = items;
        this.totalPrice = totalPrice;
        this.shippingFees = shippingFees;
        this.shippable = shippable;
        this.status = status;
        this.orderedAt = orderedAt;
    }

    public String getId() {
        return this.id;
    }

    public boolean isShippable() {
        return this.shippable;
    }

    public String getStatus(){
        if (status == OrderStatus.SHIPPED){
            return "SHIPPED";
        }
        else if (status == OrderStatus.RECEIVED){
            return "RECEIVED";
        }
        return "PROCESSING";
    }

    public void setStatus(OrderStatus status){
        this.status = status;
    }

    public void display(){
        System.out.println("** ORDER #" + this.id + " **");
        System.out.println("STATUS: " + this.getStatus());
        System.out.println("OrderedAt: " + this.orderedAt.toString());

        if (this.shippable){
            System.out.println("\n** Shipment Notice **");
            float totalweight = 0f;
            for  (CartItem item : this.items){
                Shippable shippableItem = (Shippable) item.product;
                totalweight += shippableItem.getWeight();
                System.out.println(item.getQuantity() + "X " + shippableItem.getName() + "\t" + shippableItem.getWeight());
            }

            System.out.println("----------------");
            System.out.println("Total Package Weight: " + totalweight);
        }

        System.out.println("\n** Order Items **");
        for (CartItem item : this.items){
            System.out.println(item.getQuantity() + "X " + item.product.getName() + "\t" + item.getPrice());
        }

        System.out.println("---------------");
        System.out.println("Subtotal Price: " + this.totalPrice);
        if (this.shippable)
            System.out.println("Shipping Fees: " + this.shippingFees);
        System.out.println("\nOrder Price: " + (this.totalPrice +  this.shippingFees) + "\n\n");
    }

    public void ConfirmOrder(){
        this.setStatus(OrderStatus.RECEIVED);
    }

    public void ShipOrder(){
        if (this.isShippable()){
            this.setStatus(OrderStatus.SHIPPED);
        }
        else {
            System.out.println("\nOrder Not Shippable");
        }
    }

    @Override
    public String toString() {
        return "ORDER #" + this.id;
    }
}
