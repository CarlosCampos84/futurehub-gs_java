package br.com.futurehub.futurehubgs.application.dto;

import java.time.LocalDateTime;

public record MissaoResponse(
        String id,
        String descricao,
        String objetivo,
        String moral,
        String areaId,
        String areaNome,
        boolean geradaPorIa,
        LocalDateTime dataCriacao
) {}
