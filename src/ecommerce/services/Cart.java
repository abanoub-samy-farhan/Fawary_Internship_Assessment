package ecommerce.services;

import ecommerce.models.Customer;
import ecommerce.models.Expirable;
import ecommerce.models.Product;
import ecommerce.models.Shippable;
import ecommerce.utils.ProductNodeFound;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    Map<String, CartItem> products = new HashMap<>();
    private float totalPrice;
    private float shippingFees;

    public float getTotalPrice() {
        return totalPrice;
    }

    public void addItem(String itemId, int quantity, Inventory inv) {
        CartItem item;
        if (quantity <= 0){
            System.out.println("Please enter a valid quantity");
            return;
        }
        if (products.containsKey(itemId)) {
            CartItem oldItem = products.get(itemId);
            totalPrice -= oldItem.getPrice();
            oldItem.setQuantity(quantity + oldItem.getQuantity());
            totalPrice += oldItem.getPrice();
            return;
        } else {
            Product product = inv.findProduct(itemId);
            if (product == null){
                System.out.println("Product with id " + itemId + " not found");
                return;
            }
            item = new CartItem(product, quantity);
            products.put(itemId, item);
        }


        this.totalPrice += item.getPrice();

        // if shippable, edit the shipping fees the fees
        if (item.product instanceof Shippable shippableItem) {
            shippingFees += ShippingService.calculateShippingFees(shippableItem, item.getQuantity());
        }
    }

    public void removeItem(String itemId, int quantity){
        if (!products.containsKey(itemId)) return;
        CartItem oldItem = products.get(itemId);
        int removedQuantity = Math.min(quantity, oldItem.getQuantity());
        oldItem.setQuantity(oldItem.getQuantity() - removedQuantity);
        // remove the item if it's quantity is zero
        if (oldItem.getQuantity() <= 0) {
            products.remove(itemId);
        }

        this.totalPrice -= removedQuantity * oldItem.product.getPrice();

        // if shippable, edit the shipping fees
        if (oldItem.product instanceof Shippable) {
            Shippable shippableItem = (Shippable) oldItem.product;
            shippingFees -= ShippingService.calculateShippingFees(shippableItem, removedQuantity);
        }
    }

    // Checks

    public ArrayList<CartItem> checkExpiredItems(){
        ArrayList<CartItem> expiredItems = new ArrayList<CartItem>();
        for(CartItem item: products.values()){
            if (item.product instanceof Expirable){
                if (((Expirable) item.product).isExpired()) expiredItems.add(item);
            }
        }
        return (expiredItems.isEmpty()) ? null : expiredItems;
    }

    public Map<CartItem, Integer> checkAmounts(Inventory inv){
        // return a list of the product with insufficient amounts in the inventory
        Map<CartItem, Integer> insufficientProducts = new HashMap<>();
        for(CartItem item: products.values()){
            int realAmount = inv.checkAmount(item.product.getId());
            if (realAmount < item.getQuantity()) insufficientProducts.put(item, realAmount);
        }
        return  (insufficientProducts.isEmpty()) ? null : insufficientProducts;
    }

    public void viewChart() {
        if (products.isEmpty()) {
            System.out.println("Cart is empty right now.");
            return;
        }
        System.out.println("** Chart Items **\n");
        System.out.println("Number of items: " +  products.size());
        System.out.println("------------------");
        for (CartItem item: products.values()) {
            System.out.println(item.getQuantity() + "X " + item.product.getName() + "(" +
                    item.product.getId() + ")" + "\t" + item.getPrice());
        }
        System.out.println("------------------");
        System.out.println("Total Price : " + totalPrice);
        if (shippingFees > 0){
            System.out.println("Shipping Fees : " + shippingFees);
        }


        ArrayList<CartItem> expiredItems = this.checkExpiredItems();
        if (expiredItems != null) {
            System.out.println("WARNING: Some of the products in the cart has expired");
            for  (CartItem item: expiredItems) {
                System.out.println("\t- " + item.product.getName() + " (" + item.product.getId() + ")");
            }
            System.out.println("Make sure to remove them by using the command 'remove [productId] [quantity]' before checkout");
        }
    }

    // clear
    public void clear(){
        products.clear();
        totalPrice = 0;
        shippingFees = 0;
    }

    // checkout
    public void checkout(Customer user, Inventory inv) throws ProductNodeFound {
        if (products.isEmpty()) {
            System.out.println("Cart is empty right now.");
            return;
        }
        ArrayList<CartItem> expiredItems = this.checkExpiredItems();
        Map<CartItem, Integer> insufficientProducts = this.checkAmounts(inv);

        if (expiredItems == null && insufficientProducts == null){
            if (user.getBalance() < this.totalPrice + this.shippingFees) {
                System.out.println("Your balance is insufficient, you got " + user.getBalance() + " and the order is worth" +
                        (this.totalPrice + this.shippingFees));
                return;
            }

            // remove items from the inventory and split the items into two categories
            user.setBalance(user.getBalance() - this.totalPrice -  this.shippingFees);
            ArrayList<CartItem> shippableProducts = new ArrayList<>();
            float shippablePrice = 0;
            ArrayList<CartItem> nonShippableProducts = new ArrayList<>();
            float nonShippablePrice = 0;
            for (CartItem item: products.values()) {
                if  (item.product instanceof Shippable) {
                    shippableProducts.add(item);
                    shippablePrice += item.product.getPrice() * item.getQuantity();
                } else {
                    nonShippableProducts.add(item);
                    nonShippablePrice += item.product.getPrice() * item.getQuantity();
                }

                inv.removeFromInventory(item.product.getId(), item.getQuantity());
            }

            // place the orders
            if (!shippableProducts.isEmpty()){
                String id = "ORD" + String.valueOf(Order.orderCount);
                Order.orderCount++;
                Order shippableOrder = new Order(id, shippableProducts, shippablePrice,
                        shippingFees, true, OrderStatus.PROCESSING, LocalDate.now());
                shippableOrder.display();
                user.addOrder(shippableOrder);
            }
            if (!nonShippableProducts.isEmpty()){
                String id = "ORD" + String.valueOf(Order.orderCount);
                Order.orderCount++;
                Order nonShippableOrder = new Order(id, nonShippableProducts, nonShippablePrice, 0,
                        false,  OrderStatus.PROCESSING, LocalDate.now());
                nonShippableOrder.display();
                user.addOrder(nonShippableOrder);
            }

            System.out.println("\nTransaction done successfully\n");
            this.clear();
            return;
        }

        System.out.println("\nTransaction Failed\n");
        System.out.println("-------------------------------");
        if (expiredItems != null){
            System.out.println("Some of the products in the cart has expired");
            for  (CartItem item: expiredItems) {
                System.out.println("\t- " + item.product.getName() + " (" + item.product.getId() + ")");
            }
        }
        if (insufficientProducts != null){

            System.out.println("Some of the products in the cart are either out of stock or with insufficient amount");
            System.out.println("Items out of stock are automatically removed from the cart, Here is a list of insufficient amount products (if any)");
            System.out.println("-------------------------------------------------");
            for (CartItem item: insufficientProducts.keySet()) {
                if (insufficientProducts.get(item) == 0) {
                    System.out.println("Removing " +  item.product.getName() + " (" + item.product.getId() + ")" +
                            " because it's out of stock");
                } else {
                    System.out.println("Product: " + item.toString() + " Available: " + insufficientProducts.get(item));
                }
            }
        }
        System.out.println("Make sure to remove them by using the command 'remove [productId] [quantity]' before checkout again\n");
    }
}
