package br.com.futurehub.futurehubgs.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "rankings",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id_usuario", "periodo"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private Integer pontuacaoTotal;

    @Column(nullable = false, length = 20) // EX.: 2025-11 (mensal) ou 2025-W46 (semanal)
    private String periodo;
}
