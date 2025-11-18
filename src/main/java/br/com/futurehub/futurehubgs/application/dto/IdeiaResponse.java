package br.com.futurehub.futurehubgs.application.dto;

import java.time.LocalDateTime;

public record IdeiaResponse(
        Long id,
        String titulo,
        String descricao,
        Long autorId,
        String autorNome,
        Long missaoId,
        Double mediaNotas,
        Integer totalAvaliacoes,
        LocalDateTime createdAt
) {}

