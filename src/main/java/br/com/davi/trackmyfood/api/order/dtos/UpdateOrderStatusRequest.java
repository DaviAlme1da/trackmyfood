package br.com.davi.trackmyfood.api.order.dtos;

import br.com.davi.trackmyfood.core.enums.StatusOrder;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequest (

        @NotNull
        StatusOrder status,

        Long idDeliveryMan
){ }
