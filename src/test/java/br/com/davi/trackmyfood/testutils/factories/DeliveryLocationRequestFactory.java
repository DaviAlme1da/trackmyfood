package br.com.davi.trackmyfood.testutils.factories;

import br.com.davi.trackmyfood.api.deliveryLocation.dtos.DeliveryLocationRequest;

public class DeliveryLocationRequestFactory {

    public static DeliveryLocationRequest create() {
        return DeliveryLocationRequest.builder()
                .idOrder(1L)
                .idDeliveryMan(1L)
                .latitude(-23.550520)
                .longitude(-46.633308)
                .build();
    }
}
