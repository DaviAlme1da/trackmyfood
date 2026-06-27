package br.com.davi.trackmyfood.testutils.factories;

import br.com.davi.trackmyfood.api.auth.dtos.RegisterRequest;
import br.com.davi.trackmyfood.core.enums.UserRole;

public class RegisterRequestFactory {

    public static RegisterRequest createCustomer() {
        return RegisterRequest.builder()
                .name("João Silva")
                .email("joao@email.com")
                .password("senha123")
                .role(UserRole.ROLE_CUSTOMER)
                .build();
    }

    public static RegisterRequest createDeliveryMan() {
        return RegisterRequest.builder()
                .name("Carlos Entregador")
                .email("carlos@email.com")
                .password("senha123")
                .role(UserRole.ROLE_DELIVERY_MAN)
                .build();
    }
}
