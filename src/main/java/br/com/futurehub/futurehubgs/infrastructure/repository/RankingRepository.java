package br.com.futurehub.futurehubgs.infrastructure.repository;

import br.com.futurehub.futurehubgs.domain.Ranking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RankingRepository extends MongoRepository<Ranking, String> {

    Optional<Ranking> findByUsuarioIdAndPeriodo(String usuarioId, String periodo);

    List<Ranking> findByPeriodoOrderByPontuacaoTotalDesc(String periodo);
}



