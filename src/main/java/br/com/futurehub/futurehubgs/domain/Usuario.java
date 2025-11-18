package br.com.futurehub.futurehubgs.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 180)
    private String email;

    @Column(nullable = false, length = 255)
    private String senhaHash;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.ROLE_USER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_area_interesse")
    private Area areaInteresse;

    @Builder.Default
    @Column(nullable = false)
    private Integer pontos = 0;

    public enum Role {
        ROLE_USER,
        ROLE_ADMIN
    }
}
