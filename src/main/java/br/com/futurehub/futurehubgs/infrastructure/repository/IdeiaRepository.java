package br.com.futurehub.futurehubgs.infrastructure.repository;

import br.com.futurehub.futurehubgs.domain.Ideia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IdeiaRepository extends MongoRepository<Ideia, String> {

    Page<Ideia> findByAutorId(String autorId, Pageable pageable);

    Page<Ideia> findByTituloContainingIgnoreCase(String q, Pageable pageable);

    Page<Ideia> findByAutorIdAndTituloContainingIgnoreCase(
            String autorId, String q, Pageable pageable
    );
}




