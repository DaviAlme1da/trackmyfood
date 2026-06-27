package br.com.davi.trackmyfood.testutils.factories;

import br.com.davi.trackmyfood.api.order.dtos.OrderResponse;
import br.com.davi.trackmyfood.core.enums.StatusOrder;

import java.time.LocalDateTime;

public class OrderResponseFactory {

    public static OrderResponse create() {
        return OrderResponse.builder()
                .id(1L)
                .description("Pizza Margherita")
                .status(StatusOrder.CREATED)
                .address("Rua das Flores, 123")
                .latitudeAddress(-23.550520)
                .longitudeAddress(-46.633308)
                .creationDate(LocalDateTime.now())
                .build();
    }
}
