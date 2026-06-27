package br.com.davi.trackmyfood.testutils.factories;

import br.com.davi.trackmyfood.api.auth.dtos.AuthRequest;

public class AuthRequestFactory {

    public static AuthRequest create() {
        return AuthRequest.builder()
                .email("joao@email.com")
                .password("senha123")
                .build();
    }

    public static AuthRequest createWithWrongPassword() {
        return AuthRequest.builder()
                .email("joao@email.com")
                .password("senha_errada")
                .build();
    }
}
