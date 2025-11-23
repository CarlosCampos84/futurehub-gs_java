package br.com.futurehub.futurehubgs.application.dto;

import br.com.futurehub.futurehubgs.domain.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload de entrada para criação de usuário via POST /api/usuarios.
 * Esse usuário é o de domínio (mural/ranking), não o da camada de segurança.
 */
public record UsuarioCreateRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 4, max = 100, message = "Senha deve ter pelo menos 4 caracteres")
        String senha,

        /**
         * ID da área de interesse (Mongo) já existente na collection "areas".
         * Opcional: se vier nulo, o usuário é criado sem área.
         */
        String areaInteresseId,

        /**
         * Role opcional. Se nulo, assume ROLE_USER.
         */
        Usuario.Role role
) {
}
