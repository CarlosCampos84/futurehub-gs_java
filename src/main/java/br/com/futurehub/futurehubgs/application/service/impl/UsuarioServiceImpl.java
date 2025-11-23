package br.com.futurehub.futurehubgs.application.service.impl;

import br.com.futurehub.futurehubgs.application.dto.UsuarioCreateRequest;
import br.com.futurehub.futurehubgs.application.dto.UsuarioResponse;
import br.com.futurehub.futurehubgs.application.service.UsuarioService;
import br.com.futurehub.futurehubgs.domain.Area;
import br.com.futurehub.futurehubgs.domain.Usuario;
import br.com.futurehub.futurehubgs.infrastructure.repository.AreaRepository;
import br.com.futurehub.futurehubgs.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final AreaRepository areaRepository;

    @Override
    public UsuarioResponse criar(UsuarioCreateRequest request) {

        // 1) Verifica se já existe usuário com o mesmo e-mail
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("erro.usuario.email_ja_cadastrado");
        }

        // 2) (Opcional) valida área de interesse, se enviada
        String areaIdPersistida = null;
        if (request.areaInteresseId() != null && !request.areaInteresseId().isBlank()) {
            Area area = areaRepository.findById(request.areaInteresseId())
                    .orElseThrow(() -> new IllegalArgumentException("erro.area.nao.encontrada"));
            areaIdPersistida = area.getId();
        }

        // 3) Monta entidade de domínio
        Usuario.Role role = request.role() != null
                ? request.role()
                : Usuario.Role.ROLE_USER;

        // ⚠️ DEV: aqui apenas armazenamos a senha "como veio" em senhaHash.
        // Em produção, aplique hash (ex.: BCrypt) ANTES de salvar.
        String senhaHash = request.senha();

        Usuario novo = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senhaHash(senhaHash)
                .role(role)
                .areaInteresseId(areaIdPersistida)
                .pontos(0)
                .build();

        novo = usuarioRepository.save(novo);

        return toResponse(novo);
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole(),
                usuario.getAreaInteresseId(),
                usuario.getPontos()
        );
    }
}
