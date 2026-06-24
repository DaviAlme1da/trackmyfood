package br.com.davi.trackmyfood.api.deliveryMan.controllers;

import br.com.davi.trackmyfood.api.deliveryMan.dtos.DeliveryManRequest;
import br.com.davi.trackmyfood.api.deliveryMan.dtos.DeliveryManResponse;
import br.com.davi.trackmyfood.api.deliveryMan.service.DeliveryManService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/delivery-men")
public class DeliveryManController {

    private final DeliveryManService deliveryManService;

    @PostMapping
    public ResponseEntity<DeliveryManResponse> create(@Valid @RequestBody DeliveryManRequest deliveryManRequest){

        var deliveryMan = deliveryManService.create(deliveryManRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryMan);
    }
}
