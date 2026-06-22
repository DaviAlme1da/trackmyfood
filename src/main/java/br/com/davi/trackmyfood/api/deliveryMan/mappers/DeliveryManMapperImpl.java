package br.com.davi.trackmyfood.api.deliveryMan.mappers;

import br.com.davi.trackmyfood.api.deliveryMan.dtos.DeliveryManRequest;
import br.com.davi.trackmyfood.api.deliveryMan.dtos.DeliveryManResponse;
import br.com.davi.trackmyfood.core.model.DeliveryMan;
import org.springframework.stereotype.Component;

@Component
public class DeliveryManMapperImpl implements DeliveryManMapper{
    @Override
    public DeliveryMan toDeliveryMan(DeliveryManRequest deliveryManRequest) {
        return DeliveryMan.builder()
                .name(deliveryManRequest.name())
                .status(null)
                .build();
    }

    @Override
    public DeliveryManResponse toResponse(DeliveryMan deliveryMan) {
        return DeliveryManResponse.builder()
                .id(deliveryMan.getId())
                .name(deliveryMan.getName())
                .status(deliveryMan.getStatus())
                .build();
    }
}
