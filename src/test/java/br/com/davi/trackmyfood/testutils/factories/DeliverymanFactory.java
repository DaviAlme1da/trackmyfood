package br.com.davi.trackmyfood.testutils.factories;

import br.com.davi.trackmyfood.core.enums.StatusDeliveryMan;
import br.com.davi.trackmyfood.core.enums.UserRole;
import br.com.davi.trackmyfood.core.model.DeliveryMan;

public class DeliverymanFactory {

    public static DeliveryMan create() {
        return DeliveryMan.builder()
                .name("Carlos Entregador")
                .email("carlos@email.com")
                .password("hashed_password")
                .role(UserRole.ROLE_DELIVERY_MAN)
                .status(StatusDeliveryMan.AVAILABLE)
                .build();
    }

    public static DeliveryMan createOnDelivery() {
        return DeliveryMan.builder()
                .name("Carlos Entregador")
                .email("carlos@email.com")
                .password("hashed_password")
                .role(UserRole.ROLE_DELIVERY_MAN)
                .status(StatusDeliveryMan.ON_DELIVERY)
                .build();
    }
}
