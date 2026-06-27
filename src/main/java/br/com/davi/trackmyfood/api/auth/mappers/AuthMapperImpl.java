package br.com.davi.trackmyfood.api.auth.mappers;

import br.com.davi.trackmyfood.api.auth.dtos.AuthResponse;
import br.com.davi.trackmyfood.api.auth.dtos.RegisterRequest;
import br.com.davi.trackmyfood.core.enums.StatusDeliveryMan;
import br.com.davi.trackmyfood.core.enums.UserRole;
import br.com.davi.trackmyfood.core.model.Customer;
import br.com.davi.trackmyfood.core.model.DeliveryMan;
import br.com.davi.trackmyfood.core.model.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapperImpl implements AuthMapper {
    @Override
    public Customer toCustomer(RegisterRequest request, String hashedPassword) {
        return Customer.builder()
                .name(request.name())
                .email(request.email())
                .password(hashedPassword)
                .role(UserRole.ROLE_CUSTOMER)
                .build();

    }

    @Override
    public DeliveryMan toDeliveryMan(RegisterRequest request, String hashedPassword) {
        return DeliveryMan.builder()
                .name(request.name())
                .email(request.email())
                .password(hashedPassword)
                .role(UserRole.ROLE_DELIVERY_MAN)
                .status(StatusDeliveryMan.AVAILABLE)
                .build();
    }

    @Override
    public AuthResponse toAuthResponse(User user, String accessToken, String refreshToken) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }
}
