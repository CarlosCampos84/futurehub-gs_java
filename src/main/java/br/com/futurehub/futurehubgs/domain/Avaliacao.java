package br.com.futurehub.futurehubgs.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "avaliacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Avaliacao {

    @Id
    private String id;

    private String ideiaId;

    private Integer nota;

    private LocalDateTime dataAvaliacao;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}



