package br.com.davi.trackmyfood.api.deliveryMan.mappers;

import br.com.davi.trackmyfood.api.deliveryMan.dtos.DeliveryManRequest;
import br.com.davi.trackmyfood.api.deliveryMan.dtos.DeliveryManResponse;
import br.com.davi.trackmyfood.core.model.DeliveryMan;

public interface DeliveryManMapper {

    DeliveryMan toDeliveryMan(DeliveryManRequest deliveryManRequest);

    DeliveryManResponse toResponse(DeliveryMan deliveryMan);
}
