package ecommerce.models;

public class Product {
    private final String id;
    private String name;
    private float price;
    public Product(String id, String name, float price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    // getters and setters
    public String getName(){
        return name;
    }
    public String setName(String name){
        return this.name = name;
    }

    public float getPrice() {
        return price;
    }
    public float setPrice(float price) {
        return this.price = price;
    }

    @Override
    public String toString() {
        return this.name + "\t" + "(" + this.id + ")\t" + this.price;
    }
}
