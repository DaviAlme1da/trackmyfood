package br.com.davi.trackmyfood.messaging.publisher;

import br.com.davi.trackmyfood.api.route.dtos.RouteResponse;
import br.com.davi.trackmyfood.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(RouteResponse response){

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ROUTE_EXCHANGE,
                RabbitMQConfig.ROUTE_ROUTING_KEY,
                response
        );
    }
}
