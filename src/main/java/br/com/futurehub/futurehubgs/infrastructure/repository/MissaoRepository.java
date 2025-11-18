package br.com.futurehub.futurehubgs.infrastructure.repository;

import br.com.futurehub.futurehubgs.domain.Area;
import br.com.futurehub.futurehubgs.domain.Missao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissaoRepository extends JpaRepository<Missao, Long> {

    // 👇 Métodos que você já tinha – mantidos para compatibilidade
    List<Missao> findByArea(Area area);

    List<Missao> findByArea_Id(Long areaId);

    // 👇 Novo método usado pelo MissaoServiceImpl para paginação
    Page<Missao> findByArea_Id(Long areaId, Pageable pageable);
}
