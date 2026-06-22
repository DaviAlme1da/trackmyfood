package br.com.davi.trackmyfood.api.order.mappers;

import br.com.davi.trackmyfood.api.order.dtos.OrderRequest;
import br.com.davi.trackmyfood.api.order.dtos.OrderResponse;
import br.com.davi.trackmyfood.core.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapperImpl implements  OrderMapper{

    @Override
    public Order toOrder(OrderRequest orderRequest) {
        return Order.builder()
                .description(orderRequest.description())
                .status(null)
                .address(orderRequest.address())
                .longitudeAddress(orderRequest.longitudeAddress())
                .latitudeAddress(orderRequest.latitudeAddress())
                .build();
    }

    @Override
    public OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .description(order.getDescription())
                .status(order.getStatus())
                .address(order.getAddress())
                .latitudeAddress(order.getLatitudeAddress())
                .longitudeAddress(order.getLongitudeAddress())
                .nameDeliveryMan(order.getDeliveryMan() == null ? "Not assigned yet" : order.getDeliveryMan().getName())
                .creationDate(order.getCreationDate())
                .build();
    }
}
