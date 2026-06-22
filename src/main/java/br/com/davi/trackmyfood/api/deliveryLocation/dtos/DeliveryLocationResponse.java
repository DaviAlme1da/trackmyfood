package br.com.davi.trackmyfood.api.deliveryLocation.dtos;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record DeliveryLocationResponse(

        Long id,
        Long idOrder,
        String nameDeliveryMan,
        Double latitude,
        Double longitude,
        LocalDateTime timestamp
){}
