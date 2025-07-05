package ecommerce.models;

import java.time.LocalDate;

public class Dairy extends Product implements Expirable {
    private float weight;
    public LocalDate expireAt;
    public Dairy(String id, String name, float weight, float price, LocalDate expiration_date) {
        super(id, name, price);
        this.weight = weight;
        this.expireAt = expiration_date;
    }

    public float getWeight() {
        return weight;
    }
    public void setWeight(float weight) {
        this.weight = weight;
    }

    public boolean isExpired() {
        return this.expireAt.isBefore(LocalDate.now());
    }

    public LocalDate getExpireAt() {
        return this.expireAt;
    }
}
