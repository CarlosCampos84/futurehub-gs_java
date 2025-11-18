package br.com.futurehub.futurehubgs.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String IDEAS_QUEUE = "ideas.events";
    public static final String AVALIACOES_QUEUE = "futurehub.avaliacoes";

    @Bean
    public Queue ideasQueue() {
        // fila não durável só para demo
        return new Queue(IDEAS_QUEUE, false);
    }

    @Bean
    public Queue avaliacoesQueue() {
        // fila não durável só para demo
        return new Queue(AVALIACOES_QUEUE, false);
    }
}




