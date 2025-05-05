package br.com.fiap.micronectar.model;

import jakarta.persistence.*;
import lombok.*; // Usando imports individuais para clareza em equals/hashCode

import java.time.LocalDate; // Import para data

@Entity
@Table(name = "TBL_MNT_INVESTIDORES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idUsuario") // Baseia equals/hashCode APENAS no idUsuario
public class Investidor {

    @Id // A PK é a mesma do Usuario
    @Column(name = "id_usuario")
    private Long idUsuario;

    // Relacionamento OneToOne com Usuario, usando a PK do Usuario
    @OneToOne(fetch = FetchType.LAZY) // LAZY para performance
    @MapsId // Indica que a PK desta entidade (idUsuario) é mapeada da entidade Usuario
    @JoinColumn(name = "id_usuario") // Coluna FK que também é a PK
    private Usuario usuario; // Referência ao objeto Usuario

    @Column(name = "nr_documento", length = 20, nullable = false, unique = true)
    private String nrDocumento; // Pode ser CPF ou CNPJ

    @Column(name = "ds_perfil_investidor", length = 200)
    private String dsPerfilInvestidor; // Opcional

    @Column(name = "dt_nascimento") // No DDL SQL não é NOT NULL
    private LocalDate dtNascimento; // Data de nascimento (pessoa) ou fundação (empresa)

}