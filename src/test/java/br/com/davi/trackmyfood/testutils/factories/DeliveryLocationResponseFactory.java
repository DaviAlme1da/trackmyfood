package br.com.davi.trackmyfood.testutils.factories;

import br.com.davi.trackmyfood.api.deliveryLocation.dtos.DeliveryLocationResponse;

import java.time.LocalDateTime;

public class DeliveryLocationResponseFactory {

    public static DeliveryLocationResponse create() {
        return DeliveryLocationResponse.builder()
                .id(1L)
                .idOrder(1L)
                .nameDeliveryMan("Carlos Entregador")
                .latitude(-23.550520)
                .longitude(-46.633308)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
