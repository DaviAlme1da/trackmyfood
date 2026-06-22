package br.com.davi.trackmyfood.api.order.mappers;

import br.com.davi.trackmyfood.api.order.dtos.OrderRequest;
import br.com.davi.trackmyfood.api.order.dtos.OrderResponse;
import br.com.davi.trackmyfood.core.model.Order;

public interface OrderMapper {

     Order toOrder(OrderRequest orderRequest);

     OrderResponse toResponse(Order order);

}
