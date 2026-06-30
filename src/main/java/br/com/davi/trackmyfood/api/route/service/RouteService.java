package br.com.davi.trackmyfood.api.route.service;

import br.com.davi.trackmyfood.api.route.dtos.RouteResponse;
import br.com.davi.trackmyfood.core.exceptions.RouteCalculationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${ors.api-key}")
    private String apiKey;

    public RouteResponse calculateRoute(
            Long idOrder,
            Double originLat,
            Double originLng,
            Double destLat,
            Double destLng
    ){
        try {


            String url = String.format(
                    "https://api.openrouteservice.org/v2/directions/driving-car" +
                            "?start=%s,%s&end=%s,%s",
                    originLng, originLat, destLng, destLat
            );

            String json = restClient.get()
                    .uri(url)
                    .header("Authorization", apiKey)
                    .retrieve()
                    .body(String.class);

            var root = objectMapper.readTree(json);

            var coordinates = root.get("features").get(0).get("geometry").get("coordinates");

            List<double[]> coords = new ArrayList<>();

            for (var point : coordinates) {
                double lng = point.get(0).asDouble();
                double lat = point.get(1).asDouble();

                coords.add(new double[]{lat, lng});

            }

            return RouteResponse.builder().idOrder(idOrder).coordinates(coords).build();
        }catch (JsonProcessingException e){
            throw new RouteCalculationException();
        }

    }
}
