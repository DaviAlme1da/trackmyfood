package br.com.davi.trackmyfood.testutils.factories;

import br.com.davi.trackmyfood.api.order.dtos.OrderRequest;

import java.time.LocalDateTime;

public class OrderRequestFactory {

    public static OrderRequest create() {
        return OrderRequest.builder()
                .description("Pizza Margherita")
                .address("Rua das Flores, 123")
                .latitudeAddress(-23.550520)
                .longitudeAddress(-46.633308)
                .build();
    }
}
