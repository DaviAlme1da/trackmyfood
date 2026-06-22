package br.com.davi.trackmyfood.api.deliveryMan.dtos;

import br.com.davi.trackmyfood.core.enums.StatusDeliveryMan;
import lombok.Builder;

@Builder
public record DeliveryManResponse (

        Long id,
        StatusDeliveryMan status,
        String name
){}
