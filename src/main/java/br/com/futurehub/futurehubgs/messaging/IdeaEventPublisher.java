package br.com.futurehub.futurehubgs.messaging;

import br.com.futurehub.futurehubgs.domain.Ideia;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static br.com.futurehub.futurehubgs.config.RabbitConfig.IDEAS_QUEUE;

@Component
@RequiredArgsConstructor
public class IdeaEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishCreated(Ideia ideia) {
        String payload = "IDEIA_CRIADA#" + ideia.getId() + "#" + ideia.getTitulo();
        rabbitTemplate.convertAndSend(IDEAS_QUEUE, payload);
    }
}


