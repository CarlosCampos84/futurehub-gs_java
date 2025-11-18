package br.com.futurehub.futurehubgs.application.dto;

import jakarta.validation.constraints.*;

public record IdeiaUpdateRequest(
        @NotBlank @Size(max = 160) String titulo,
        @NotBlank @Size(max = 2000) String descricao
) {}


