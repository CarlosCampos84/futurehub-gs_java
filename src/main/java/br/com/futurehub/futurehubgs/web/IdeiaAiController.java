package br.com.futurehub.futurehubgs.web;

import br.com.futurehub.futurehubgs.application.dto.MissaoResponse;
import br.com.futurehub.futurehubgs.application.service.MissaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/missoes")
@RequiredArgsConstructor
public class IdeiaAiController {

    private final MissaoService missaoService;

    /**
     * Gera uma nova missão com IA para uma determinada área.
     *
     * Exemplo:
     *   POST /api/missoes/gerar?areaId=1
     */
    @PostMapping("/gerar")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MissaoResponse> gerarMissao(@RequestParam Long areaId,
                                                      UriComponentsBuilder uriBuilder) {

        MissaoResponse resp = missaoService.gerarMissaoPorArea(areaId);

        var location = uriBuilder.path("/api/missoes/{id}")
                .buildAndExpand(resp.id())
                .toUri();

        return ResponseEntity.created(location).body(resp);
    }

    /**
     * Lista missões (geradas ou não por IA), com paginação opcional por área.
     *
     * Exemplo:
     *   GET /api/missoes?areaId=1&page=0&size=10
     */
    @GetMapping
    public Page<MissaoResponse> listarPorArea(
            @RequestParam(required = false) Long areaId,
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return missaoService.listarPorArea(areaId, pageable);
    }
}
