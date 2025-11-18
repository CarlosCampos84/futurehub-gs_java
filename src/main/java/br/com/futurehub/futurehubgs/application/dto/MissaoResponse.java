package br.com.futurehub.futurehubgs.application.dto;

import java.time.LocalDateTime;

public record MissaoResponse(
        Long id,
        String descricao,
        String objetivo,
        String moral,
        Long areaId,
        String areaNome,
        boolean geradaPorIa,
        LocalDateTime dataCriacao
) {}
