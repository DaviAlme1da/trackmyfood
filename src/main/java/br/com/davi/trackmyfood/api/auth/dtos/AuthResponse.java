package br.com.davi.trackmyfood.api.auth.dtos;

import lombok.Builder;

@Builder
public record AuthResponse(
        Long id,
        String accessToken,
        String refreshToken,
        String name,
        String role
) {
}
