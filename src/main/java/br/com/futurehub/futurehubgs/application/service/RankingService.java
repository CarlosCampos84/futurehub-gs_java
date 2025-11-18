package br.com.futurehub.futurehubgs.application.service;

import br.com.futurehub.futurehubgs.application.dto.RankingUsuarioResponse;

import java.util.List;

/**
 * Caso de uso de Ranking do FutureHub.
 */
public interface RankingService {

    /**
     * Processa um evento de avaliação para atualizar o ranking.
     * @param ideiaId id da ideia avaliada
     * @param nota nota atribuída
     */
    void processarEventoAvaliacao(Long ideiaId, int nota);

    /**
     * Lista o ranking de um determinado período (ex.: "2025-11").
     * Se período for null/blank, usa o mês atual.
     */
    List<RankingUsuarioResponse> listarPorPeriodo(String periodo);
}
