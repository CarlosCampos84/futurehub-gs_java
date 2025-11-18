package br.com.futurehub.futurehubgs.infrastructure.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório Mongo para o documento de perfil de IA.
 * Usado para leitura/escrita de perfis agregados no dataset_ia.
 */
@Repository
public interface PerfilIARepository extends MongoRepository<PerfilIA, String> {

    Optional<PerfilIA> findByEmail(String email);
}






