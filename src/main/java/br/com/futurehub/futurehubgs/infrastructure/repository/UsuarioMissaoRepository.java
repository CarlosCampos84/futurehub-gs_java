package br.com.futurehub.futurehubgs.infrastructure.repository;

import br.com.futurehub.futurehubgs.domain.UsuarioMissao;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsuarioMissaoRepository extends MongoRepository<UsuarioMissao, String> {

    Optional<UsuarioMissao> findByUsuarioIdAndMissaoId(String usuarioId, String missaoId);
}




