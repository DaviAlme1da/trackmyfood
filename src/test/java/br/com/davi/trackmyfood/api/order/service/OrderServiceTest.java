package br.com.davi.trackmyfood.api.order.service;

import br.com.davi.trackmyfood.api.deliveryMan.service.DeliveryManService;
import br.com.davi.trackmyfood.api.order.dtos.UpdateOrderStatusRequest;
import br.com.davi.trackmyfood.api.order.mappers.OrderMapper;
import br.com.davi.trackmyfood.api.route.dtos.RouteResponse;
import br.com.davi.trackmyfood.api.route.service.RouteService;
import br.com.davi.trackmyfood.core.enums.StatusDeliveryMan;
import br.com.davi.trackmyfood.core.enums.StatusOrder;
import br.com.davi.trackmyfood.core.exceptions.BusinessException;
import br.com.davi.trackmyfood.core.exceptions.OrderNotFoundException;
import br.com.davi.trackmyfood.core.repository.DeliveryLocationRepository;
import br.com.davi.trackmyfood.core.repository.OrderRepository;
import br.com.davi.trackmyfood.core.services.auth.UserDetailsImpl;
import br.com.davi.trackmyfood.messaging.publisher.RouteEventPublisher;
import br.com.davi.trackmyfood.testutils.factories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private DeliveryManService deliveryManService;

    @Mock
    private DeliveryLocationRepository deliveryLocationRepository;

    @Mock
    private RouteService routeService;

    @Mock
    private RouteEventPublisher routeEventPublisher;

    @InjectMocks
    private OrderService orderService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void authenticateAs(br.com.davi.trackmyfood.core.model.User user) {
        var userDetails = new UserDetailsImpl(user);
        var authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("create: deve criar pedido com sucesso quando usuário é Customer")
    void create_shouldCreateOrder_whenUserIsCustomer() {
        var customer = CustomerFactory.create();
        var request = OrderRequestFactory.create();
        var order = OrderFactory.create();
        var response = OrderResponseFactory.create();

        authenticateAs(customer);

        given(orderMapper.toOrder(request)).willReturn(order);
        given(orderRepository.save(any())).willReturn(order);
        given(orderMapper.toResponse(order)).willReturn(response);

        var result = orderService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.description()).isEqualTo("Pizza Margherita");
        assertThat(result.status()).isEqualTo(StatusOrder.CREATED);

        verify(orderRepository).save(any());
        verify(orderMapper).toResponse(order);
    }

    @Test
    @DisplayName("create: deve lançar BusinessException quando usuário é DeliveryMan")
    void create_shouldThrowBusinessException_whenUserIsDeliveryMan() {
        var deliveryMan = DeliverymanFactory.create();
        var request = OrderRequestFactory.create();

        authenticateAs(deliveryMan);

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Only customers can create orders");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("findById: deve retornar OrderResponse quando pedido existe")
    void findById_shouldReturnOrderResponse_whenOrderExists() {
        var order = OrderFactory.create();
        var response = OrderResponseFactory.create();

        given(orderRepository.findById(1L)).willReturn(Optional.of(order));
        given(orderMapper.toResponse(order)).willReturn(response);

        var result = orderService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.description()).isEqualTo("Pizza Margherita");
    }

    @Test
    @DisplayName("findById: deve lançar OrderNotFoundException quando pedido não existe")
    void findById_shouldThrowOrderNotFoundException_whenOrderDoesNotExist() {
        given(orderRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.findById(99L))
                .isInstanceOf(OrderNotFoundException.class);

        verify(orderMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("updateStatus: deve lançar BusinessException quando OUT_FOR_DELIVERY sem idDeliveryMan")
    void updateStatus_shouldThrowBusinessException_whenOutForDeliveryWithoutDeliveryMan() {
        var order = OrderFactory.create();
        var request = new UpdateOrderStatusRequest(StatusOrder.OUT_FOR_DELIVERY, null);

        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.updateStatus(1L, request))
                .isInstanceOf(BusinessException.class);

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateStatus: deve associar entregador e mudar status quando OUT_FOR_DELIVERY")
    void updateStatus_shouldAssignDeliveryManAndChangeStatus_whenOutForDelivery() {
        var order = OrderFactory.create();
        var deliveryMan = DeliverymanFactory.create();
        var request = new UpdateOrderStatusRequest(StatusOrder.OUT_FOR_DELIVERY, 1L);

        given(orderRepository.findById(1L)).willReturn(Optional.of(order));
        given(deliveryManService.findById(1L)).willReturn(deliveryMan);
        given(deliveryLocationRepository.findTopByDeliveryManIdOrderByTimestampDesc(deliveryMan.getId()))
                .willReturn(Optional.empty());

        orderService.updateStatus(1L, request);

        assertThat(order.getStatus()).isEqualTo(StatusOrder.OUT_FOR_DELIVERY);
        assertThat(order.getDeliveryMan()).isEqualTo(deliveryMan);

        verify(deliveryManService).updateStatusDeliveryMan(
                deliveryMan.getId(),
                StatusDeliveryMan.ON_DELIVERY
        );
        verify(orderRepository).save(order);
        verify(routeService, never()).calculateRoute(any(), any(), any(), any(), any());
        verify(routeEventPublisher, never()).publish(any());
    }

    @Test
    @DisplayName("updateStatus: deve calcular e publicar rota usando o id do pedido atual quando OUT_FOR_DELIVERY e há localização anterior do entregador")
    void updateStatus_shouldCalculateRouteWithCurrentOrderId_whenOutForDeliveryAndDeliveryManHasLocation() {
        var order = OrderFactory.create();
        order.setId(1L);

        var previousOrder = OrderFactory.create();
        previousOrder.setId(99L);

        var deliveryMan = DeliverymanFactory.create();
        var previousLocation = DeliveryLocationFactory.create();
        previousLocation.setOrder(previousOrder);

        var request = new UpdateOrderStatusRequest(StatusOrder.OUT_FOR_DELIVERY, 1L);
        var routeResponse = RouteResponse.builder().idOrder(1L).coordinates(java.util.List.of()).build();

        given(orderRepository.findById(1L)).willReturn(Optional.of(order));
        given(deliveryManService.findById(1L)).willReturn(deliveryMan);
        given(deliveryLocationRepository.findTopByDeliveryManIdOrderByTimestampDesc(deliveryMan.getId()))
                .willReturn(Optional.of(previousLocation));
        given(routeService.calculateRoute(
                eq(order.getId()),
                eq(previousLocation.getLatitude()),
                eq(previousLocation.getLongitude()),
                eq(order.getLatitudeAddress()),
                eq(order.getLongitudeAddress())
        )).willReturn(routeResponse);

        orderService.updateStatus(1L, request);

        verify(routeService).calculateRoute(
                eq(order.getId()),
                eq(previousLocation.getLatitude()),
                eq(previousLocation.getLongitude()),
                eq(order.getLatitudeAddress()),
                eq(order.getLongitudeAddress())
        );
        verify(routeService, never()).calculateRoute(
                eq(previousOrder.getId()), any(), any(), any(), any()
        );
        verify(routeEventPublisher).publish(routeResponse);
    }

    @Test
    @DisplayName("updateStatus: deve liberar entregador quando pedido é DELIVERED")
    void updateStatus_shouldReleaseDeliveryMan_whenOrderIsDelivered() {
        var order = OrderFactory.createWithDeliveryMan();
        var request = new UpdateOrderStatusRequest(StatusOrder.DELIVERED, null);

        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        orderService.updateStatus(1L, request);

        assertThat(order.getStatus()).isEqualTo(StatusOrder.DELIVERED);

        verify(deliveryManService).updateStatusDeliveryMan(
                order.getDeliveryMan().getId(),
                StatusDeliveryMan.AVAILABLE
        );
    }

    @Test
    @DisplayName("updateStatus: deve lançar OrderNotFoundException quando pedido não existe")
    void updateStatus_shouldThrowOrderNotFoundException_whenOrderDoesNotExist() {
        var request = new UpdateOrderStatusRequest(StatusOrder.DELIVERED, null);
        given(orderRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.updateStatus(99L, request))
                .isInstanceOf(OrderNotFoundException.class);
    }
}