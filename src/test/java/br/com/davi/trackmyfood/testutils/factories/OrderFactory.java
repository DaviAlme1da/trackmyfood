package br.com.davi.trackmyfood.testutils.factories;

import br.com.davi.trackmyfood.core.enums.StatusOrder;
import br.com.davi.trackmyfood.core.model.Order;

public class OrderFactory {

    public static Order create() {
        return Order.builder()
                .description("Pizza Margherita")
                .address("Rua das Flores, 123")
                .latitudeAddress(-23.550520)
                .longitudeAddress(-46.633308)
                .status(StatusOrder.CREATED)
                .customer(CustomerFactory.create())
                .build();
    }

    public static Order createWithDeliveryMan() {
        return Order.builder()
                .description("Pizza Margherita")
                .address("Rua das Flores, 123")
                .latitudeAddress(-23.550520)
                .longitudeAddress(-46.633308)
                .status(StatusOrder.OUT_FOR_DELIVERY)
                .customer(CustomerFactory.create())
                .deliveryMan(DeliverymanFactory.createOnDelivery())
                .build();
    }
}
