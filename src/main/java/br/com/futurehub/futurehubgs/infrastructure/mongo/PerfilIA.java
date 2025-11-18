package br.com.futurehub.futurehubgs.infrastructure.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * Documento de apoio para a camada de IA:
 * consolida dados agregados do usuário (perfil, ideias e missões)
 * em uma collection própria do MongoDB.
 */
@Document(collection = "dataset_ia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilIA {

    @Id
    private String id;

    // Mantém o nome com snake_case no Mongo, mas usa camelCase no Java.
    @Field("usuario_id")
    private Long usuarioId;

    private String nome;
    private String email;
    private Integer pontos;

    /**
     * Ideias publicadas pelo usuário, com métricas agregadas.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class IdeiaPub {
        private String titulo;

        @Field("missao_id")
        private Long missaoId;

        @Field("media_notas")
        private Double mediaNotas;

        @Field("total_avaliacoes")
        private Integer totalAvaliacoes;
    }

    /**
     * Missões concluídas pelo usuário (visão resumida).
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MissaoConc {
        private String objetivo;
        private String area;
    }

    @Field("ideias_publicadas")
    private List<IdeiaPub> ideiasPublicadas;

    @Field("missoes_concluidas")
    private List<MissaoConc> missoesConcluidas;
}







