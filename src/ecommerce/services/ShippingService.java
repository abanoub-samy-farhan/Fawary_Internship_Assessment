package ecommerce.services;

import ecommerce.models.Shippable;

public class ShippingService {
    public static float calculateShippingFees(Shippable item, int quantity) {
        float weight = item.getWeight();
        float pricePerPiece = weight / 1000 * 50;
        return pricePerPiece * quantity;
    }

    public void ShipOrder(Order order){
        if (order.isShippable()) {
            System.out.println(order.toString() + "has been shipped successfully");
            order.ShipOrder();
        }
    }
}
