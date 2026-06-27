package br.com.davi.trackmyfood.api.auth.mappers;

import br.com.davi.trackmyfood.api.auth.dtos.AuthResponse;
import br.com.davi.trackmyfood.api.auth.dtos.RegisterRequest;
import br.com.davi.trackmyfood.core.model.Customer;
import br.com.davi.trackmyfood.core.model.DeliveryMan;
import br.com.davi.trackmyfood.core.model.User;

public interface AuthMapper {
    Customer toCustomer(RegisterRequest request, String hashedPassword);
    DeliveryMan toDeliveryMan(RegisterRequest request, String hashedPassword);
    AuthResponse toAuthResponse(User user, String accessToken, String refreshToken);
}
