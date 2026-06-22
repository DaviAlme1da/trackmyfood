package br.com.davi.trackmyfood.api.deliveryMan.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record DeliveryManRequest (

        @NotBlank(message = "name cannot be empty")
        String name
){}
