package br.com.futurehub.futurehubgs.application.service;

import br.com.futurehub.futurehubgs.application.dto.MissaoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MissaoService {

    /**
     * Gera uma nova missão usando IA, vinculada a uma determinada área.
     */
    MissaoResponse gerarMissaoPorArea(Long areaId);

    /**
     * Lista missões por área (ou todas, se areaId == null), com paginação.
     */
    Page<MissaoResponse> listarPorArea(Long areaId, Pageable pageable);
}
