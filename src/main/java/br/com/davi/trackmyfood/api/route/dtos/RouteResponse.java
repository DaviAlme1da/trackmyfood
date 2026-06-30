package br.com.davi.trackmyfood.api.route.dtos;

import lombok.Builder;
import java.util.List;

@Builder
public record RouteResponse(
        Long idOrder,
        List<double[]> coordinates
) {}
