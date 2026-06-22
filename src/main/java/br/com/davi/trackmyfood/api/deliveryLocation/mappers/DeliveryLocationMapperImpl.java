package br.com.davi.trackmyfood.api.deliveryLocation.mappers;

import br.com.davi.trackmyfood.api.deliveryLocation.dtos.DeliveryLocationRequest;
import br.com.davi.trackmyfood.api.deliveryLocation.dtos.DeliveryLocationResponse;
import br.com.davi.trackmyfood.core.model.DeliveryLocation;
import br.com.davi.trackmyfood.core.model.DeliveryMan;
import br.com.davi.trackmyfood.core.model.Order;
import org.springframework.stereotype.Component;

@Component
public class DeliveryLocationMapperImpl implements DeliveryLocationMapper {


    @Override
    public DeliveryLocation toDeliveryLocation(
            DeliveryLocationRequest deliveryLocationRequest,
            Order order,
            DeliveryMan deliveryMan) {

        return DeliveryLocation.builder()
                .order(order)
                .deliveryMan(deliveryMan)
                .latitude(deliveryLocationRequest.latitude())
                .longitude(deliveryLocationRequest.longitude())
                .build();
    }

    @Override
    public DeliveryLocationResponse toResponse(DeliveryLocation deliveryLocation) {
        return DeliveryLocationResponse.builder()
                .id(deliveryLocation.getId())
                .idOrder(deliveryLocation.getOrder().getId())
                .nameDeliveryMan(deliveryLocation.getDeliveryMan().getName())
                .latitude(deliveryLocation.getLatitude())
                .longitude(deliveryLocation.getLongitude())
                .timestamp(deliveryLocation.getTimestamp())
                .build();
    }
}
