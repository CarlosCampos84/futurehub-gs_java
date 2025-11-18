package br.com.futurehub.futurehubgs.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ideias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ideia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 160)
    private String titulo;

    @Column(nullable = false, length = 2000)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_missao")
    private Missao missao;

    @Builder.Default
    @Column(nullable = false)
    private Double mediaNotas = 0.0;

    @Builder.Default
    @Column(nullable = false)
    private Integer totalAvaliacoes = 0;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (mediaNotas == null) {
            mediaNotas = 0.0;
        }
        if (totalAvaliacoes == null) {
            totalAvaliacoes = 0;
        }
    }
}
