package br.com.futurehub.futurehubgs.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "ideias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ideia {

    @Id
    private String id;

    private String titulo;

    private String descricao;

    private String autorId;

    private String missaoId;

    @Builder.Default
    private Double mediaNotas = 0.0;

    @Builder.Default
    private Integer totalAvaliacoes = 0;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

