package br.com.futurehub.futurehubgs.application.service;

import br.com.futurehub.futurehubgs.application.dto.IdeiaCreateRequest;
import br.com.futurehub.futurehubgs.application.dto.IdeiaResponse;
import br.com.futurehub.futurehubgs.application.dto.IdeiaUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IdeiaService {

    IdeiaResponse criar(IdeiaCreateRequest req);

    Page<IdeiaResponse> listar(String areaId, String q, Pageable pageable);

    IdeiaResponse buscar(String id);

    IdeiaResponse atualizar(String id, IdeiaUpdateRequest req);

    void deletar(String id);
}
