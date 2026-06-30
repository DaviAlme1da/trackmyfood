package br.com.davi.trackmyfood.api.order.service;

import br.com.davi.trackmyfood.api.deliveryMan.service.DeliveryManService;
import br.com.davi.trackmyfood.api.order.dtos.OrderRequest;
import br.com.davi.trackmyfood.api.order.dtos.OrderResponse;
import br.com.davi.trackmyfood.api.order.dtos.UpdateOrderStatusRequest;
import br.com.davi.trackmyfood.api.order.mappers.OrderMapper;
import br.com.davi.trackmyfood.api.route.service.RouteService;
import br.com.davi.trackmyfood.core.enums.StatusDeliveryMan;
import br.com.davi.trackmyfood.core.enums.StatusOrder;
import br.com.davi.trackmyfood.core.enums.UserRole;
import br.com.davi.trackmyfood.core.exceptions.BusinessException;
import br.com.davi.trackmyfood.core.exceptions.OrderNotFoundException;
import br.com.davi.trackmyfood.core.model.Customer;
import br.com.davi.trackmyfood.core.model.Order;
import br.com.davi.trackmyfood.core.repository.DeliveryLocationRepository;
import br.com.davi.trackmyfood.core.repository.OrderRepository;
import br.com.davi.trackmyfood.core.services.auth.UserDetailsImpl;
import br.com.davi.trackmyfood.messaging.publisher.RouteEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final RouteService routeService;
    private final OrderRepository orderRepository;
    private final DeliveryManService deliveryManService;
    private final RouteEventPublisher routeEventPublisher;
    private final DeliveryLocationRepository deliveryLocationRepository;


    public OrderResponse create(OrderRequest orderRequest){

        var order = orderMapper.toOrder(orderRequest);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if(userDetails.getUser().getRole().equals(UserRole.ROLE_DELIVERY_MAN)){
            throw new BusinessException("Only customers can create orders");
        }

        var customer = (Customer) userDetails.getUser();

        order.setCustomer(customer);
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

            var locationDeliveryMan = deliveryLocationRepository.findTopByDeliveryManIdOrderByTimestampDesc(deliveryMan.getId());

            if (locationDeliveryMan.isPresent()) {

                var route = routeService.calculateRoute(
                        order.getId(),
                        locationDeliveryMan.get().getLatitude(),
                        locationDeliveryMan.get().getLongitude(),
                        order.getLatitudeAddress(),
                        order.getLongitudeAddress()
                );

                routeEventPublisher.publish(route);

            }

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

    public List<OrderResponse> listAvailableOrders() {
        return orderRepository.findAllByStatus(StatusOrder.CREATED)
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    public List<OrderResponse> listMyOrders() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var customer = (Customer) userDetails.getUser();
        return orderRepository.findAllByCustomerId(customer.getId())
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }
}
