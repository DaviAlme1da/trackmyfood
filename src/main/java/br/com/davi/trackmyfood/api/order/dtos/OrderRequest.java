package br.com.davi.trackmyfood.api.order.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record OrderRequest(
        @NotBlank(message = "Description cannot be empty")
        String description,

        @NotBlank(message = "Address cannot be empty")
        String address,

        @NotNull
        Double latitudeAddress,

        @NotNull
        Double longitudeAddress
) {}
