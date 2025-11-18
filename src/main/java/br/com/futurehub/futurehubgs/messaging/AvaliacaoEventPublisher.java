package br.com.futurehub.futurehubgs.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static br.com.futurehub.futurehubgs.config.RabbitConfig.AVALIACOES_QUEUE;

@Component
@RequiredArgsConstructor
public class AvaliacaoEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishAvaliacao(String ideiaId, int nota) {
        String payload = ideiaId + ";" + nota;
        rabbitTemplate.convertAndSend(AVALIACOES_QUEUE, payload);
    }
}



