package br.com.futurehub.futurehubgs.application.service;

import br.com.futurehub.futurehubgs.application.dto.RankingUsuarioResponse;
import java.util.List;

public interface RankingService {

    void processarEventoAvaliacao(String ideiaId, int nota);

    List<RankingUsuarioResponse> listarPorPeriodo(String periodo);
}
