package br.com.futurehub.futurehubgs.web;

import br.com.futurehub.futurehubgs.application.dto.IdeiaCreateRequest;
import br.com.futurehub.futurehubgs.application.dto.IdeiaResponse;
import br.com.futurehub.futurehubgs.application.dto.IdeiaUpdateRequest;
import br.com.futurehub.futurehubgs.application.service.IdeiaService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/ideias")
@RequiredArgsConstructor
public class IdeiaController {

    private final IdeiaService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<IdeiaResponse> criar(
            @Valid @RequestBody IdeiaCreateRequest req,
            UriComponentsBuilder uri
    ) {
        var resp = service.criar(req);

        var location = uri.path("/api/ideias/{id}")
                .buildAndExpand(resp.id())
                .toUri();

        return ResponseEntity.created(location).body(resp);
    }

    @GetMapping
    public Page<IdeiaResponse> listar(
            @RequestParam(required = false) String areaId,
            @RequestParam(required = false) String q,
            @PageableDefault(
                    size = 20,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return service.listar(areaId, q, pageable);
    }

    @GetMapping("/{id}")
    public IdeiaResponse buscar(@PathVariable String id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public IdeiaResponse atualizar(@PathVariable String id,
                                   @Valid @RequestBody IdeiaUpdateRequest req) {
        return service.atualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}





