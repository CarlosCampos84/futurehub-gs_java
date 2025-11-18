package br.com.futurehub.futurehubgs.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static br.com.futurehub.futurehubgs.config.RabbitConfig.IDEAS_QUEUE;

@Component
@Slf4j
public class IdeaEventListener {

    @RabbitListener(queues = IDEAS_QUEUE)
    public void onMessage(String payload) {
        log.info("Evento recebido da fila de ideias: {}", payload);
    }
}





