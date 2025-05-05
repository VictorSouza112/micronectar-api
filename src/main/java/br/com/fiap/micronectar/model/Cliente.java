package br.com.fiap.micronectar.model;

import jakarta.persistence.*;
import lombok.*; // Usar individuais para controle
import org.springframework.format.annotation.DateTimeFormat; // Para formatar data se necessário em DTOs

import java.math.BigDecimal;
import java.time.LocalDate; // Usar LocalDate para data sem hora

@Entity
@Table(name = "TBL_MNT_CLIENTES", uniqueConstraints = {
        @UniqueConstraint(name = "UK_CLIENTE_CPF", columnNames = "nr_cpf") // Garante CPF único a nível de banco
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idUsuario") // Baseia equals/hashCode no ID compartilhado
public class Cliente {

    @Id // A PK é a mesma do Usuario
    @Column(name = "id_usuario")
    private Long idUsuario;

    // Relacionamento OneToOne com Usuario (Obrigatório para um Cliente existir)
    @OneToOne(fetch = FetchType.LAZY, optional = false) // optional=false indica que todo Cliente TEM um Usuario
    @MapsId // Mapeia a PK desta entidade para a PK da entidade Usuario associada
    @JoinColumn(name = "id_usuario") // Coluna que é PK e FK
    @ToString.Exclude // Evitar recursão no toString
    private Usuario usuario; // Referência ao objeto Usuario correspondente

    @Column(name = "nr_cpf", length = 14, nullable = false /* unique garantido pela constraint */)
    private String nrCpf;

    @Column(name = "dt_nascimento", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // Formato esperado: YYYY-MM-DD
    private LocalDate dtNascimento; // Usar LocalDate para apenas data

    @Column(name = "nr_media_avaliacao", precision = 5, scale = 2)
    private BigDecimal nrMediaAvaliacao = BigDecimal.ZERO; // Padrão é 0

}