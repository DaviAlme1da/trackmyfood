package br.com.davi.trackmyfood.api.deliveryLocation.service;

import br.com.davi.trackmyfood.api.deliveryLocation.dtos.DeliveryLocationRequest;
import br.com.davi.trackmyfood.api.deliveryLocation.dtos.DeliveryLocationResponse;
import br.com.davi.trackmyfood.api.deliveryLocation.mappers.DeliveryLocationMapper;
import br.com.davi.trackmyfood.api.deliveryMan.service.DeliveryManService;
import br.com.davi.trackmyfood.api.order.service.OrderService;
import br.com.davi.trackmyfood.core.repository.DeliveryLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryLocationService {

    private final DeliveryLocationMapper deliveryLocationMapper;
    private final DeliveryLocationRepository deliveryLocationRepository;
    private final DeliveryManService deliveryManService;
    private final OrderService orderService;


    public DeliveryLocationResponse registerLocation(
            DeliveryLocationRequest deliveryLocationRequest
    ){
        var order = orderService.findById(deliveryLocationRequest.idOrder());
        var deliveryMan = deliveryManService.findById(deliveryLocationRequest.idDeliveryMan());

       var deliveryLocation = deliveryLocationMapper.toDeliveryLocation(
               deliveryLocationRequest,
               order,
               deliveryMan
       );

       var deliveryLocationSave = deliveryLocationRepository.save(deliveryLocation);

       return deliveryLocationMapper.toResponse(deliveryLocationSave);
    }

    public List<DeliveryLocationResponse> findOrderLocationHistoryById(Long idOrder){
        return  deliveryLocationRepository.findByOrderIdOrderByTimestampAsc(idOrder)
                .stream()
                .map(deliveryLocationMapper::toResponse)
                .toList();
    }

}
