package br.com.davi.trackmyfood.api.deliveryLocation.controllers;

import br.com.davi.trackmyfood.api.deliveryLocation.dtos.DeliveryLocationRequest;
import br.com.davi.trackmyfood.api.deliveryLocation.dtos.DeliveryLocationResponse;
import br.com.davi.trackmyfood.api.deliveryLocation.service.DeliveryLocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/delivery-locations")
public class DeliveryLocationController {

    private final DeliveryLocationService deliveryLocationService;

    @PostMapping
    public ResponseEntity<DeliveryLocationResponse> registrarLocation(
            @Valid @RequestBody DeliveryLocationRequest deliveryLocationRequest
    ){

        var deliveryLocation = deliveryLocationService.registerLocation(deliveryLocationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryLocation);

    }

    @GetMapping("/order/{idOrder}")
    public ResponseEntity<List<DeliveryLocationResponse>> findLocationHistoryByOrder(
            @PathVariable Long idOrder
            ){

        var orderLocation = deliveryLocationService.findOrderLocationHistoryById(idOrder);

        return ResponseEntity.status(HttpStatus.OK).body(orderLocation);

    }

}
