package br.com.fiap.micronectar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "TBL_MNT_MICROEMPREENDEDORES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Microempreendedor {

    @Id // A PK é a mesma do Usuario
    @Column(name = "id_usuario")
    private Long idUsuario;

    // Relacionamento OneToOne com Usuario.
    // @MapsId diz que a PK desta entidade (idUsuario) é mapeada/derivada
    // da chave primária da entidade Usuario associada.
    // @JoinColumn especifica a coluna que armazena a FK (que também é a PK aqui).
    @OneToOne(fetch = FetchType.LAZY) // LAZY é geralmente bom para desempenho
    @MapsId
    @JoinColumn(name = "id_usuario")
    private Usuario usuario; // Referência ao objeto Usuario correspondente

    @Column(name = "nr_cnpj", length = 18, nullable = false, unique = true)
    private String nrCnpj;

    @Column(name = "nm_fundador", length = 150)
    private String nmFundador;

    @Column(name = "ds_categoria", length = 100)
    private String dsCategoria;

    @Lob // Indica que deve ser mapeado para um tipo de objeto grande (CLOB no Oracle)
    @Column(name = "ds_historia")
    private String dsHistoria;

    @Lob
    @Column(name = "ds_historia_fundador")
    private String dsHistoriaFundador;

    @Column(name = "pitch_url", length = 500)
    private String pitchUrl;

    // Usando tipos primitivos para que inicializem com 0 por padrão em Java,
    // alinhado com o DEFAULT 0 do banco.
    @Column(name = "qt_votos_promissor", nullable = false)
    private int qtVotosPromissor = 0; // Inicializa em Java também

    @Column(name = "nr_media_avaliacao", precision = 5, scale = 2)
    private BigDecimal nrMediaAvaliacao = BigDecimal.ZERO; // Usar BigDecimal e inicializar com ZERO

}