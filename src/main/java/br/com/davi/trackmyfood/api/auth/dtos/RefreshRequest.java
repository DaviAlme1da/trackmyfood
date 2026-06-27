package br.com.davi.trackmyfood.api.auth.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RefreshRequest(

        @NotBlank
        String refreshToken
) {
}
