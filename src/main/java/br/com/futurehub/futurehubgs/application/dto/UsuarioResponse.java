package br.com.futurehub.futurehubgs.application.dto;

import br.com.futurehub.futurehubgs.domain.Usuario;

/**
 * DTO de saída retornado pelo endpoint de criação de usuário.
 */
public record UsuarioResponse(
        String id,
        String nome,
        String email,
        Usuario.Role role,
        String areaInteresseId,
        Integer pontos
) {
}
