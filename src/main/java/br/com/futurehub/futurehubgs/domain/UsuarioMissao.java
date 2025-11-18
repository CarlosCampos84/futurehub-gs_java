package br.com.futurehub.futurehubgs.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "usuarios_missoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioMissao {

    @Id
    private String id;

    private String usuarioId;

    private String missaoId;

    private LocalDateTime dataConclusao;

    private String status;
}



