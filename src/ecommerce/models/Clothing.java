package ecommerce.models;

public class Clothing extends Product implements Shippable{
    private float weight;
    public Clothing(String id, String name, float price, float weight) {
        super(id, name, price);
        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }
    public void setWeight(float weight) {
        this.weight = weight;
    }

}
