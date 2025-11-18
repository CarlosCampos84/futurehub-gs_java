package br.com.futurehub.futurehubgs.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rankings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ranking {

    @Id
    private String id;

    private String usuarioId;

    private Integer pontuacaoTotal;

    private String periodo;
}



