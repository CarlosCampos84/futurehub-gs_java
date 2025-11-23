package br.com.futurehub.futurehubgs.web;

import br.com.futurehub.futurehubgs.application.dto.UsuarioCreateRequest;
import br.com.futurehub.futurehubgs.application.dto.UsuarioResponse;
import br.com.futurehub.futurehubgs.application.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Controller responsável pelo cadastro de usuários de domínio
 * (aqueles que publicam ideias, recebem missões, entram no ranking).
 *
 * Endpoint principal:
 *  - POST /api/usuarios
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(
            @Valid @RequestBody UsuarioCreateRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        UsuarioResponse resp = usuarioService.criar(request);

        var location = uriBuilder
                .path("/api/usuarios/{id}")
                .buildAndExpand(resp.id())
                .toUri();

        return ResponseEntity.created(location).body(resp);
    }
}



