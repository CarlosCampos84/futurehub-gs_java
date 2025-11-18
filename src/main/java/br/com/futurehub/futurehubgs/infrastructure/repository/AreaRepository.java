package br.com.futurehub.futurehubgs.infrastructure.repository;

import br.com.futurehub.futurehubgs.domain.Area;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AreaRepository extends MongoRepository<Area, String> {

    Optional<Area> findByNomeIgnoreCase(String nome);
}



