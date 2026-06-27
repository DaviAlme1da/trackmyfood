package br.com.davi.trackmyfood.messaging.publisher;

import br.com.davi.trackmyfood.api.deliveryLocation.dtos.DeliveryLocationResponse;
import br.com.davi.trackmyfood.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(DeliveryLocationResponse response){

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                response
        );
    }
}
