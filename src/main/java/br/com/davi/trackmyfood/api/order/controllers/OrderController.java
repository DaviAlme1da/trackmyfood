package br.com.davi.trackmyfood.api.order.controllers;

import br.com.davi.trackmyfood.api.order.dtos.OrderRequest;
import br.com.davi.trackmyfood.api.order.dtos.OrderResponse;
import br.com.davi.trackmyfood.api.order.dtos.UpdateOrderStatusRequest;
import br.com.davi.trackmyfood.api.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody OrderRequest orderRequest) {

            var order = orderService.create(orderRequest);

            return  ResponseEntity.status(HttpStatus.CREATED).body(order);

    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @Valid @RequestBody UpdateOrderStatusRequest updateOrderStatusRequest,
            @PathVariable Long id){

            orderService.updateStatus(id, updateOrderStatusRequest);
            return ResponseEntity.status(HttpStatus.OK).build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id){

            var order = orderService.findById(id);
            return  ResponseEntity.status(HttpStatus.OK).body(order);

    }
}

