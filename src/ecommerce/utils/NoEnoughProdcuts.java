package ecommerce.utils;

public class NoEnoughProdcuts extends RuntimeException {
    public NoEnoughProdcuts(String message) {
        super(message);
    }
}
