package br.com.davi.trackmyfood.api.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthRequest(

        @Email
        @NotBlank
        String email,

        @NotBlank
        String password
) {
}
