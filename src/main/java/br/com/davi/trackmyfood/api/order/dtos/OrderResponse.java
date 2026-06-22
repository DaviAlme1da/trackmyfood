package br.com.davi.trackmyfood.api.order.dtos;

import br.com.davi.trackmyfood.core.enums.StatusOrder;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record OrderResponse(
        Long id,
        String description,
        StatusOrder status,
        String address,
        Double latitudeAddress,
        Double longitudeAddress,
        String nameDeliveryMan,
        LocalDateTime creationDate
) {}