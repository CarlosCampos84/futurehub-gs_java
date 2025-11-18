package br.com.futurehub.futurehubgs.infrastructure.repository;

import br.com.futurehub.futurehubgs.domain.Missao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MissaoRepository extends MongoRepository<Missao, String> {

    List<Missao> findByAreaId(String areaId);

    Page<Missao> findByAreaId(String areaId, Pageable pageable);
}


