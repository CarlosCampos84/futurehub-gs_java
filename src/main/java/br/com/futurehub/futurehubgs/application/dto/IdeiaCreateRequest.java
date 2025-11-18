package br.com.futurehub.futurehubgs.application.dto;

import jakarta.validation.constraints.*;

public record IdeiaCreateRequest(
        @NotBlank @Size(max = 160) String titulo,
        @NotBlank @Size(max = 2000) String descricao,
        @NotNull String idUsuario,   // agora idUsuario é String (Mongo)
        String idMissao              // idMissao também vira String
) {}
