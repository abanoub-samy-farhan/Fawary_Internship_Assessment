package ecommerce.models;

public class Electronic extends Product implements Shippable {
    private float weight;
    public Electronic(String id, String name, float weight, float price) {
        super(id, name, price);
        this.weight = weight;
    }

    public float getWeight() {
        return this.weight;
    }
    public void setWeight(float weight) {
        this.weight = weight;
    }
}
