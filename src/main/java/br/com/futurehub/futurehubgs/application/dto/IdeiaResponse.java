package br.com.futurehub.futurehubgs.application.dto;

import java.time.LocalDateTime;

public record IdeiaResponse(
        String id,
        String titulo,
        String descricao,
        String autorId,
        String autorNome,
        String missaoId,
        Double mediaNotas,
        Integer totalAvaliacoes,
        LocalDateTime createdAt
) {}
