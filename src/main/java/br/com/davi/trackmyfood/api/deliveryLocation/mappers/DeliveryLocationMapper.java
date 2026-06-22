package br.com.davi.trackmyfood.api.deliveryLocation.mappers;

import br.com.davi.trackmyfood.api.deliveryLocation.dtos.DeliveryLocationRequest;
import br.com.davi.trackmyfood.api.deliveryLocation.dtos.DeliveryLocationResponse;
import br.com.davi.trackmyfood.core.model.DeliveryLocation;
import br.com.davi.trackmyfood.core.model.DeliveryMan;
import br.com.davi.trackmyfood.core.model.Order;


public interface DeliveryLocationMapper {

    DeliveryLocation toDeliveryLocation(DeliveryLocationRequest deliveryLocationRequest, Order order, DeliveryMan deliveryMan);

    DeliveryLocationResponse toResponse(DeliveryLocation deliveryLocation);
}
