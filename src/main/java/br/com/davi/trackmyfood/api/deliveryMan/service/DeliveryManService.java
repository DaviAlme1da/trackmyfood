package br.com.davi.trackmyfood.api.deliveryMan.service;

import br.com.davi.trackmyfood.api.deliveryMan.dtos.DeliveryManRequest;
import br.com.davi.trackmyfood.api.deliveryMan.dtos.DeliveryManResponse;
import br.com.davi.trackmyfood.api.deliveryMan.mappers.DeliveryManMapper;
import br.com.davi.trackmyfood.core.enums.StatusDeliveryMan;
import br.com.davi.trackmyfood.core.exceptions.DeliveryManNotFoundException;
import br.com.davi.trackmyfood.core.model.DeliveryMan;
import br.com.davi.trackmyfood.core.repository.DeliveryManRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryManService {

    private final DeliveryManMapper deliveryManMapper;
    private final DeliveryManRepository deliveryManRepository;


    public DeliveryManResponse create(DeliveryManRequest deliveryManRequest){

        var deliveryMan = deliveryManMapper.toDeliveryMan(deliveryManRequest);

        deliveryMan.setStatus(StatusDeliveryMan.AVAILABLE);

        var deliveryManSave = deliveryManRepository.save(deliveryMan);

        return  deliveryManMapper.toResponse(deliveryManSave);

    }

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
