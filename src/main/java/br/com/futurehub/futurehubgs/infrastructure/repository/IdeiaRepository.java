package br.com.futurehub.futurehubgs.infrastructure.repository;

import br.com.futurehub.futurehubgs.domain.Ideia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdeiaRepository extends JpaRepository<Ideia, Long> {

    Page<Ideia> findByAutor_AreaInteresse_Id(Long areaId, Pageable pageable);

    Page<Ideia> findByTituloContainingIgnoreCase(String q, Pageable pageable);

    Page<Ideia> findByAutor_AreaInteresse_IdAndTituloContainingIgnoreCase(
            Long areaId, String q, Pageable pageable
    );
}
