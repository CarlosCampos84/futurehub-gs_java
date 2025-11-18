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

    @PostMapping("/gerar")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MissaoResponse> gerarMissao(
            @RequestParam String areaId,
            UriComponentsBuilder uriBuilder
    ) {
        var resp = missaoService.gerarMissaoPorArea(areaId);

        var location = uriBuilder.path("/api/missoes/{id}")
                .buildAndExpand(resp.id())
                .toUri();

        return ResponseEntity.created(location).body(resp);
    }

    @GetMapping
    public Page<MissaoResponse> listarPorArea(
            @RequestParam(required = false) String areaId,
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return missaoService.listarPorArea(areaId, pageable);
    }
}




