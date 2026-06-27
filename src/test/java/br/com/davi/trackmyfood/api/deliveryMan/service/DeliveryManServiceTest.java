package br.com.davi.trackmyfood.api.deliveryMan.service;

import br.com.davi.trackmyfood.core.enums.StatusDeliveryMan;
import br.com.davi.trackmyfood.core.exceptions.DeliveryManNotFoundException;
import br.com.davi.trackmyfood.core.repository.DeliveryManRepository;
import br.com.davi.trackmyfood.testutils.factories.DeliverymanFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeliveryManServiceTest {

    @Mock
    private DeliveryManRepository deliveryManRepository;

    @InjectMocks
    private DeliveryManService deliveryManService;


    @Test
    @DisplayName("findById: deve retornar DeliveryMan quando existe")
    void findById_shouldReturnDeliveryMan_whenExists() {
        var deliveryMan = DeliverymanFactory.create();
        given(deliveryManRepository.findById(1L)).willReturn(Optional.of(deliveryMan));

        var result = deliveryManService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Carlos Entregador");
        assertThat(result.getStatus()).isEqualTo(StatusDeliveryMan.AVAILABLE);
    }

    @Test
    @DisplayName("findById: deve lançar DeliveryManNotFoundException quando não existe")
    void findById_shouldThrowDeliveryManNotFoundException_whenNotExists() {
        given(deliveryManRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> deliveryManService.findById(99L))
                .isInstanceOf(DeliveryManNotFoundException.class);
    }


    @Test
    @DisplayName("updateStatusDeliveryMan: deve atualizar status para ON_DELIVERY")
    void updateStatusDeliveryMan_shouldUpdateStatus_toOnDelivery() {

        var deliveryMan = DeliverymanFactory.create();
        given(deliveryManRepository.findById(1L)).willReturn(Optional.of(deliveryMan));

        deliveryManService.updateStatusDeliveryMan(1L, StatusDeliveryMan.ON_DELIVERY);

        assertThat(deliveryMan.getStatus()).isEqualTo(StatusDeliveryMan.ON_DELIVERY);
        verify(deliveryManRepository).save(deliveryMan);
    }

    @Test
    @DisplayName("updateStatusDeliveryMan: deve atualizar status para AVAILABLE")
    void updateStatusDeliveryMan_shouldUpdateStatus_toAvailable() {

        var deliveryMan = DeliverymanFactory.createOnDelivery();
        given(deliveryManRepository.findById(1L)).willReturn(Optional.of(deliveryMan));

        deliveryManService.updateStatusDeliveryMan(1L, StatusDeliveryMan.AVAILABLE);

        assertThat(deliveryMan.getStatus()).isEqualTo(StatusDeliveryMan.AVAILABLE);
        verify(deliveryManRepository).save(deliveryMan);
    }

    @Test
    @DisplayName("updateStatusDeliveryMan: deve lançar DeliveryManNotFoundException quando entregador não existe")
    void updateStatusDeliveryMan_shouldThrowDeliveryManNotFoundException_whenNotExists() {

        given(deliveryManRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> deliveryManService.updateStatusDeliveryMan(99L, StatusDeliveryMan.AVAILABLE))
                .isInstanceOf(DeliveryManNotFoundException.class);

        verify(deliveryManRepository).findById(99L);
    }
}