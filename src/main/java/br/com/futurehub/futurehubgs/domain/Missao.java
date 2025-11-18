package br.com.futurehub.futurehubgs.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "missoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Missao {

    @Id
    private String id;

    private String descricao;

    private String objetivo;

    private String moral;

    private LocalDateTime dataCriacao;

    private String areaId;

    private String status;

    private boolean geradaPorIa;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
