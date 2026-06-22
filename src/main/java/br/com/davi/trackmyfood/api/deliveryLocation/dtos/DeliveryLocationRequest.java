package br.com.davi.trackmyfood.api.deliveryLocation.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DeliveryLocationRequest(

        @NotNull
        Long idOrder,
        @NotNull
        Long idDeliveryMan,
        @NotNull
        Double latitude,
        @NotNull
        Double longitude
){}
