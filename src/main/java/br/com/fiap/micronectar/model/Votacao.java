package br.com.fiap.micronectar.model;

import br.com.fiap.micronectar.config.jpa.BooleanToStringSNConverter; // Importar o Converter
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_MNT_VOTACOES",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_votacao_unica",
                columnNames = {"id_votador_usuario", "id_microempreendedor"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idVotacao")
public class Votacao {

    // ... (outros campos e anotações id, votador, meVotado, dtVotacao) ...

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "votacao_seq")
    @SequenceGenerator(name = "votacao_seq", sequenceName = "SEQ_MNT_VOTACAO", allocationSize = 1)
    @Column(name = "id_votacao")
    private Long idVotacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_votador_usuario", nullable = false)
    private Usuario votador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_microempreendedor", nullable = false)
    private Microempreendedor microempreendedorVotado;

    @CreationTimestamp
    @Column(name = "dt_votacao", nullable = false, updatable = false)
    private LocalDateTime dtVotacao;

    @Convert(converter = BooleanToStringSNConverter.class) // Aplica o conversor customizado
    @Column(name = "fl_promissor", length = 1, nullable = false)
    private Boolean promissor = false; // O tipo Java continua Boolean!

    @Column(name = "ds_comentario", length = 500)
    private String comentario;
}