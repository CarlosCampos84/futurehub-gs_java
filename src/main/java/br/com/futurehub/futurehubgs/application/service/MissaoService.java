package br.com.futurehub.futurehubgs.application.service;

import br.com.futurehub.futurehubgs.application.dto.MissaoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MissaoService {

    MissaoResponse gerarMissaoPorArea(String areaId);

    Page<MissaoResponse> listarPorArea(String areaId, Pageable pageable);
}
