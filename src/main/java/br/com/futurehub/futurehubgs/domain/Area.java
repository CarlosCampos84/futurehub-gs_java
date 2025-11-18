package br.com.futurehub.futurehubgs.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "areas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Area {

    @Id
    private String id;

    private String nome;

    private String descricao;
}


