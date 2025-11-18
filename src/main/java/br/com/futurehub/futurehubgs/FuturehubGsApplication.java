package br.com.futurehub.futurehubgs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Classe principal da aplicação Spring Boot.
 */
@SpringBootApplication
@EnableMongoRepositories(basePackages = "br.com.futurehub.futurehubgs.infrastructure.mongo")
public class FuturehubGsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FuturehubGsApplication.class, args);
    }
}


