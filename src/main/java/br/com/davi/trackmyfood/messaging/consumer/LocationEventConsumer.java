package br.com.davi.trackmyfood.messaging.consumer;

import br.com.davi.trackmyfood.api.deliveryLocation.dtos.DeliveryLocationResponse;
import br.com.davi.trackmyfood.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationEventConsumer {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleLocationEvent (DeliveryLocationResponse response){

        simpMessagingTemplate.convertAndSend(
                String.format("/topic/order/%d",response.idOrder()),
                response);
    }
}
