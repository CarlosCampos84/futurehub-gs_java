package br.com.futurehub.futurehubgs.infrastructure.repository;

import br.com.futurehub.futurehubgs.domain.Avaliacao;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AvaliacaoRepository extends MongoRepository<Avaliacao, String> {

    List<Avaliacao> findByIdeiaId(String ideiaId);
}



