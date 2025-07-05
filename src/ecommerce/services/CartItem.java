package ecommerce.services;

import ecommerce.models.Product;

public class CartItem {
    public Product product;
    private int quantity;

    CartItem(Product p, int q){
        this.product = p;
        this.quantity = q;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int q) {
        this.quantity = q;
    }

    public float getPrice(){
        return this.product.getPrice()  * quantity;
    }

    @Override
    public String toString() {
        return product.getName() + "(" + product.getId() + ") " + quantity;
    }
}
