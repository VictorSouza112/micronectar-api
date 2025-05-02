package br.com.fiap.micronectar.model;

import jakarta.persistence.*;
import lombok.*; // Import específico Lombok

import java.math.BigDecimal; // Import para vl_produto

@Entity
@Table(name = "TBL_MNT_PRODUTOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idProduto") // Basear equals/hashCode no ID
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "produto_seq")
    @SequenceGenerator(name = "produto_seq", sequenceName = "SEQ_MNT_PRODUTO", allocationSize = 1)
    @Column(name = "id_produto")
    private Long idProduto;

    @Column(name = "nm_produto", length = 150, nullable = false)
    private String nmProduto;

    // Usar BigDecimal para valores monetários ou decimais precisos
    @Column(name = "vl_produto", precision = 10, scale = 2, nullable = false)
    private BigDecimal vlProduto;

    // Usar Integer para quantidade, permitir nulo conforme DDL
    @Column(name = "qt_produto")
    private Integer qtProduto;

    @Column(name = "ds_produto", length = 1000)
    private String dsProduto;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    // Relacionamento Muitos-para-Um: Muitos Produtos pertencem a Um Microempreendedor
    @ManyToOne(fetch = FetchType.LAZY) // LAZY para performance
    @JoinColumn(name = "id_microempreendedor", nullable = false) // Nome da coluna FK no banco TBL_MNT_PRODUTOS
    @ToString.Exclude // Evitar recursão no toString
    private Microempreendedor microempreendedor; // Referência à entidade Microempreendedor

}