package br.com.davi.trackmyfood.testutils.factories;

import br.com.davi.trackmyfood.core.enums.UserRole;
import br.com.davi.trackmyfood.core.model.Customer;

public class CustomerFactory {

    public static Customer create() {
        return Customer.builder()
                .name("João Silva")
                .email("joao@email.com")
                .password("hashed_password")
                .role(UserRole.ROLE_CUSTOMER)
                .build();
    }

    public static Customer createWithId() {
        return Customer.builder()
                .name("João Silva")
                .email("joao@email.com")
                .password("hashed_password")
                .role(UserRole.ROLE_CUSTOMER)
                .build();
    }
}
