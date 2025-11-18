package br.com.futurehub.futurehubgs.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    private String id;

    private String nome;

    private String email;

    private String senhaHash;

    private Role role;

    private String areaInteresseId;

    @Builder.Default
    private Integer pontos = 0;

    public enum Role {
        ROLE_USER,
        ROLE_ADMIN
    }
}
