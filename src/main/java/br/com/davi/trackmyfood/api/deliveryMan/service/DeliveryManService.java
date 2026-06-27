package br.com.davi.trackmyfood.api.deliveryMan.service;

import br.com.davi.trackmyfood.api.deliveryMan.dtos.DeliveryManResponse;
import br.com.davi.trackmyfood.core.enums.StatusDeliveryMan;
import br.com.davi.trackmyfood.core.exceptions.DeliveryManNotFoundException;
import br.com.davi.trackmyfood.core.model.DeliveryMan;
import br.com.davi.trackmyfood.core.repository.DeliveryManRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryManService {

    private final DeliveryManRepository deliveryManRepository;

    public DeliveryMan findById(Long id){
        return deliveryManRepository.findById(id)
                .orElseThrow(DeliveryManNotFoundException::new);
    }

    public void updateStatusDeliveryMan(Long idDeliveryMan, StatusDeliveryMan novoStatus){

        var deliveryMan = findById(idDeliveryMan);
        deliveryMan.setStatus(novoStatus);
        deliveryManRepository.save(deliveryMan);

    }

}
