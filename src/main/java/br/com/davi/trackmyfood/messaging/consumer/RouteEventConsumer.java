package br.com.davi.trackmyfood.messaging.consumer;

import br.com.davi.trackmyfood.api.route.dtos.RouteResponse;
import br.com.davi.trackmyfood.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RouteEventConsumer {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @RabbitListener(queues = RabbitMQConfig.ROUTE_QUEUE)
    public void handleLocationEvent (RouteResponse response){

        simpMessagingTemplate.convertAndSend(
                String.format("/topic/route/%d",response.idOrder()),
                response);
    }
}
