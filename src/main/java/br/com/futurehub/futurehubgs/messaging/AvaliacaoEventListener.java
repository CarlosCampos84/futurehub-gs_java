package br.com.futurehub.futurehubgs.messaging;

import br.com.futurehub.futurehubgs.application.service.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static br.com.futurehub.futurehubgs.config.RabbitConfig.AVALIACOES_QUEUE;

/**
 * Listener de eventos de avaliação.
 * Responsabilidade única: receber mensagem da fila e delegar para o RankingService.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AvaliacaoEventListener {

    private final RankingService rankingService;

    @RabbitListener(queues = AVALIACOES_QUEUE)
    public void onMessage(String payload) {
        try {
            String[] parts = payload.split(";");
            Long ideiaId = Long.parseLong(parts[0]);
            int nota = Integer.parseInt(parts[1]);

            log.info("Evento de avaliação recebido: ideiaId={}, nota={}", ideiaId, nota);
            rankingService.processarEventoAvaliacao(ideiaId, nota);
        } catch (Exception e) {
            log.error("Erro ao processar evento de avaliação. Payload: {}", payload, e);
        }
    }
}
