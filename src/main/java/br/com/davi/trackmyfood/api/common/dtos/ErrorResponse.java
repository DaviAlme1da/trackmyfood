package br.com.davi.trackmyfood.api.common.dtos;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        String message,

        LocalDateTime timestamp
) { }
