package br.com.davi.trackmyfood.testutils.factories;

import br.com.davi.trackmyfood.api.auth.dtos.AuthResponse;

public class AuthResponseFactory {

    public static AuthResponse create() {
        return AuthResponse.builder()
                .accessToken("access_token_fake")
                .refreshToken("refresh_token_fake")
                .name("João Silva")
                .role("ROLE_CUSTOMER")
                .build();
    }
}
