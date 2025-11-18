package br.com.futurehub.futurehubgs.infrastructure.repository;

import br.com.futurehub.futurehubgs.domain.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}




