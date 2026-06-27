package br.com.davi.trackmyfood.api.deliveryLocation.service;


import br.com.davi.trackmyfood.api.deliveryLocation.mappers.DeliveryLocationMapper;
import br.com.davi.trackmyfood.api.deliveryMan.service.DeliveryManService;
import br.com.davi.trackmyfood.api.order.service.OrderService;
import br.com.davi.trackmyfood.core.repository.DeliveryLocationRepository;
import br.com.davi.trackmyfood.testutils.factories.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeliveryLocationServiceTest {

    @Mock
    private DeliveryLocationMapper deliveryLocationMapper;

    @Mock
    private DeliveryLocationRepository deliveryLocationRepository;

    @Mock
    private DeliveryManService deliveryManService;

    @Mock
    private OrderService orderService;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private DeliveryLocationService deliveryLocationService;

    @Test
    @DisplayName("registerLocation: deve salvar localização e publicar no WebSocket")
    void registerLocation_shouldSaveAndPublishToWebSocket() {
        var request = DeliveryLocationRequestFactory.create();
        var order = OrderFactory.create();
        var deliveryMan = DeliverymanFactory.create();
        var deliveryLocation = DeliveryLocationFactory.create();
        var response = DeliveryLocationResponseFactory.create();

        given(orderService.getOrderEntityById(request.idOrder())).willReturn(order);
        given(deliveryManService.findById(request.idDeliveryMan())).willReturn(deliveryMan);
        given(deliveryLocationMapper.toDeliveryLocation(request, order, deliveryMan))
                .willReturn(deliveryLocation);
        given(deliveryLocationRepository.save(deliveryLocation)).willReturn(deliveryLocation);
        given(deliveryLocationMapper.toResponse(deliveryLocation)).willReturn(response);

        var result = deliveryLocationService.registerLocation(request);

        assertThat(result).isNotNull();
        assertThat(result.latitude()).isEqualTo(-23.550520);
        assertThat(result.longitude()).isEqualTo(-46.633308);
        assertThat(result.idOrder()).isEqualTo(1L);

        verify(deliveryLocationRepository).save(deliveryLocation);

        verify(simpMessagingTemplate).convertAndSend(
                eq("/topic/order/1"),
                eq(response)
        );
    }

    @Test
    @DisplayName("registerLocation: deve buscar Order e DeliveryMan antes de salvar")
    void registerLocation_shouldFetchOrderAndDeliveryMan_beforeSaving() {
        var request = DeliveryLocationRequestFactory.create();
        var order = OrderFactory.create();
        var deliveryMan = DeliverymanFactory.create();
        var deliveryLocation = DeliveryLocationFactory.create();
        var response = DeliveryLocationResponseFactory.create();

        given(orderService.getOrderEntityById(1L)).willReturn(order);
        given(deliveryManService.findById(1L)).willReturn(deliveryMan);
        given(deliveryLocationMapper.toDeliveryLocation(request, order, deliveryMan))
                .willReturn(deliveryLocation);
        given(deliveryLocationRepository.save(any())).willReturn(deliveryLocation);
        given(deliveryLocationMapper.toResponse(deliveryLocation)).willReturn(response);

        deliveryLocationService.registerLocation(request);

        verify(orderService).getOrderEntityById(1L);
        verify(deliveryManService).findById(1L);

        verify(deliveryLocationMapper).toDeliveryLocation(request, order, deliveryMan);
    }

    @Test
    @DisplayName("registerLocation: deve publicar no tópico correto baseado no idOrder")
    void registerLocation_shouldPublishToCorrectTopic_basedOnOrderId() {
        var request = DeliveryLocationRequestFactory.create();
        var order = OrderFactory.create();
        var deliveryMan = DeliverymanFactory.create();
        var deliveryLocation = DeliveryLocationFactory.create();
        var response = DeliveryLocationResponseFactory.create();

        given(orderService.getOrderEntityById(1L)).willReturn(order);
        given(deliveryManService.findById(1L)).willReturn(deliveryMan);
        given(deliveryLocationMapper.toDeliveryLocation(any(), any(), any())).willReturn(deliveryLocation);
        given(deliveryLocationRepository.save(any())).willReturn(deliveryLocation);
        given(deliveryLocationMapper.toResponse(any())).willReturn(response);

        deliveryLocationService.registerLocation(request);

        verify(simpMessagingTemplate).convertAndSend(
                eq("/topic/order/1"),
                (Object) any()
        );
    }

    @Test
    @DisplayName("findOrderLocationHistoryById: deve retornar lista de localizações em ordem cronológica")
    void findOrderLocationHistoryById_shouldReturnLocationList_inChronologicalOrder() {
        var location1 = DeliveryLocationFactory.create();
        var location2 = DeliveryLocationFactory.create();
        var response1 = DeliveryLocationResponseFactory.create();
        var response2 = DeliveryLocationResponseFactory.create();

        given(deliveryLocationRepository.findByOrderIdOrderByTimestampAsc(1L))
                .willReturn(List.of(location1, location2));
        given(deliveryLocationMapper.toResponse(location1)).willReturn(response1);
        given(deliveryLocationMapper.toResponse(location2)).willReturn(response2);

        var result = deliveryLocationService.findOrderLocationHistoryById(1L);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);

        verify(deliveryLocationMapper).toResponse(location1);
        verify(deliveryLocationMapper).toResponse(location2);
    }

    @Test
    @DisplayName("findOrderLocationHistoryById: deve retornar lista vazia quando não há localizações")
    void findOrderLocationHistoryById_shouldReturnEmptyList_whenNoLocations() {
        given(deliveryLocationRepository.findByOrderIdOrderByTimestampAsc(99L))
                .willReturn(List.of());

        var result = deliveryLocationService.findOrderLocationHistoryById(99L);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }
}