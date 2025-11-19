package br.com.futurehub.futurehubgs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 👇 IMPORTANTE: exclusões para desativar completamente o JPA
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Classe principal da aplicação Spring Boot.
 * MongoDB agora é o repositório principal.
 * JPA/Postgres ficam desabilitados (apenas standby no pom).
 */
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@EnableMongoRepositories(basePackages = "br.com.futurehub.futurehubgs.infrastructure.repository")
public class FuturehubGsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FuturehubGsApplication.class, args);
    }
}
