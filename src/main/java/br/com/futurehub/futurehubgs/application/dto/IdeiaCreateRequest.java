package br.com.futurehub.futurehubgs.application.dto;

import jakarta.validation.constraints.*;

public record IdeiaCreateRequest(
        @NotBlank @Size(max = 160) String titulo,
        @NotBlank @Size(max = 2000) String descricao,
        @NotNull Long idUsuario,
        Long idMissao
) {}

