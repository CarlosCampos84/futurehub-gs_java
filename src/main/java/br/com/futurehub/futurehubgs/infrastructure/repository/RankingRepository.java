package br.com.futurehub.futurehubgs.infrastructure.repository;

import br.com.futurehub.futurehubgs.domain.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RankingRepository extends JpaRepository<Ranking, Long> {

    Optional<Ranking> findByUsuario_IdAndPeriodo(Long usuarioId, String periodo);

    List<Ranking> findByPeriodoOrderByPontuacaoTotalDesc(String periodo);
}
