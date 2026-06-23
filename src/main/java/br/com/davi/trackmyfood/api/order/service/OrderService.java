package br.com.davi.trackmyfood.api.order.service;

import br.com.davi.trackmyfood.api.deliveryMan.service.DeliveryManService;
import br.com.davi.trackmyfood.api.order.dtos.OrderRequest;
import br.com.davi.trackmyfood.api.order.dtos.OrderResponse;
import br.com.davi.trackmyfood.api.order.dtos.UpdateOrderStatusRequest;
import br.com.davi.trackmyfood.api.order.mappers.OrderMapper;
import br.com.davi.trackmyfood.core.enums.StatusDeliveryMan;
import br.com.davi.trackmyfood.core.enums.StatusOrder;
import br.com.davi.trackmyfood.core.exceptions.BusinessException;
import br.com.davi.trackmyfood.core.exceptions.OrderNotFoundException;
import br.com.davi.trackmyfood.core.model.Order;
import br.com.davi.trackmyfood.core.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final DeliveryManService deliveryManService;

    public OrderResponse create(OrderRequest orderRequest){

        var order = orderMapper.toOrder(orderRequest);

        order.setStatus(StatusOrder.CREATED);

        var orderSave = orderRepository.save(order);

        return  orderMapper.toResponse(orderSave);

    }

    public void updateStatus(Long idOrder, UpdateOrderStatusRequest updateOrderStatusRequest){
        var order = getOrderEntityById(idOrder);

        if(updateOrderStatusRequest.status().equals(StatusOrder.OUT_FOR_DELIVERY)){
            if(updateOrderStatusRequest.idDeliveryMan() == null){
                throw new BusinessException();
            }
            var deliveryMan = deliveryManService.findById(updateOrderStatusRequest.idDeliveryMan());
            order.setDeliveryMan(deliveryMan);

            deliveryManService.updateStatusDeliveryMan(deliveryMan.getId(), StatusDeliveryMan.ON_DELIVERY);
        }

        if (updateOrderStatusRequest.status().equals(StatusOrder.DELIVERED) ||
                        updateOrderStatusRequest.status().equals(StatusOrder.CANCELED)){
            if (order.getDeliveryMan() != null){
                deliveryManService.updateStatusDeliveryMan(
                        order.getDeliveryMan().getId(),
                        StatusDeliveryMan.AVAILABLE);
            }
        }

        order.setStatus(updateOrderStatusRequest.status());

        orderRepository.save(order);


    }

    public Order getOrderEntityById(Long idOrder){

        return orderRepository.findById(idOrder)
                .orElseThrow(OrderNotFoundException::new);
    }

    public OrderResponse findById(Long idOrder){

        return orderRepository.findById(idOrder)
                .map(orderMapper::toResponse)
                .orElseThrow(OrderNotFoundException::new);
    }

}
