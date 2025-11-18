package br.com.futurehub.futurehubgs.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios_missoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(UsuarioMissao.PK.class)
public class UsuarioMissao {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_missao")
    private Missao missao;

    private LocalDateTime dataConclusao;

    @Column(length = 30) // EM_ANDAMENTO, CONCLUIDA
    private String status;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = "EM_ANDAMENTO";
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PK implements Serializable {
        private Long usuario;
        private Long missao;
    }
}
