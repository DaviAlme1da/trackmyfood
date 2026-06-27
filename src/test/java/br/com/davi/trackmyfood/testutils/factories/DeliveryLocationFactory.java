package br.com.davi.trackmyfood.testutils.factories;

import br.com.davi.trackmyfood.core.model.DeliveryLocation;

import java.time.LocalDateTime;

public class DeliveryLocationFactory {

    public static DeliveryLocation create() {
        return DeliveryLocation.builder()
                .order(OrderFactory.create())
                .deliveryMan(DeliverymanFactory.create())
                .latitude(-23.550520)
                .longitude(-46.633308)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
