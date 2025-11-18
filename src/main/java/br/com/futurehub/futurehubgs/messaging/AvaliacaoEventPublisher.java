package br.com.futurehub.futurehubgs.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static br.com.futurehub.futurehubgs.config.RabbitConfig.AVALIACOES_QUEUE;

/**
 * Publica eventos de avaliação na fila do RabbitMQ.
 * Responsabilidade única: transformar o domínio em mensagem para a fila.
 */
@Component
@RequiredArgsConstructor
public class AvaliacaoEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Envia um evento simples contendo id da ideia e nota aplicada.
     * Formato do payload: "ideiaId;nota"
     */
    public void publishAvaliacao(Long ideiaId, int nota) {
        String payload = ideiaId + ";" + nota;
        rabbitTemplate.convertAndSend(AVALIACOES_QUEUE, payload);
    }
}
