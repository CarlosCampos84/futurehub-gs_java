package br.com.futurehub.futurehubgs.infrastructure.repository;

import br.com.futurehub.futurehubgs.domain.UsuarioMissao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioMissaoRepository extends JpaRepository<UsuarioMissao, UsuarioMissao.PK> {

    Optional<UsuarioMissao> findByUsuario_IdAndMissao_Id(Long usuarioId, Long missaoId);
}
