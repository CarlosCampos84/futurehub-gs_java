package br.com.futurehub.futurehubgs.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "missoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Missao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 250)
    private String descricao;

    @Column(nullable = false, length = 250)
    private String objetivo;

    @Column(length = 120)
    private String moral;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_area", nullable = false)
    private Area area;

    @Column(nullable = false, length = 30) // EX.: ATIVA, ENCERRADA
    private String status;

    /**
     * Indica se a missão foi gerada por IA generativa.
     */
    @Column(nullable = false)
    private boolean geradaPorIa;

    @PrePersist
    public void prePersist() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
        if (status == null) {
            status = "ATIVA";
        }
        // geradaPorIa permanece com o default do Java (false) se não for setado.
    }
}
