package br.com.fiap.micronectar.model;

import br.com.fiap.micronectar.enums.TipoUsuario;
import br.com.fiap.micronectar.enums.UsuarioRole; // Import do novo Enum
import jakarta.persistence.*;
import lombok.*; // Imports específicos do Lombok
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority; // Import UserDetails
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Import UserDetails
import org.springframework.security.core.userdetails.UserDetails; // Import UserDetails

import java.time.LocalDateTime;
import java.util.Collection; // Import UserDetails
import java.util.List; // Import UserDetails

@Entity
@Table(name = "TBL_MNT_USUARIOS", uniqueConstraints = { // Garante a constraint unique a nível de entidade
        @UniqueConstraint(name = "UK_USUARIO_EMAIL", columnNames = "email")
})
// Usar anotações individuais do Lombok para controlar equals/hashCode
@Getter // Gera getters
@Setter // Gera setters
@NoArgsConstructor // Gera construtor sem argumentos
@AllArgsConstructor // Gera construtor com todos os argumentos
@EqualsAndHashCode(of = "idUsuario") // Baseia equals/hashCode APENAS no idUsuario
public class Usuario implements UserDetails { // Implementa UserDetails

    @Id // Marca como chave primária
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq") // Define estratégia de geração via sequence
    @SequenceGenerator(name = "usuario_seq", sequenceName = "SEQ_MNT_USUARIO", allocationSize = 1) // Configura a sequence
    @Column(name = "id_usuario") // Mapeia para a coluna id_usuario
    private Long idUsuario;

    @Column(name = "nome", length = 150, nullable = false) // Mapeia para a coluna nome
    private String nome;

    @Column(name = "email", length = 100, nullable = false, unique = true) // Mapeia para a coluna email
    private String email;

    @Column(name = "senha", length = 100, nullable = false) // Mapeia para a coluna senha (texto plano por agora)
    private String senha;

    @Enumerated(EnumType.STRING) // Persiste o Enum como String ('CLIENTE', 'INVESTIDOR', ...)
    @Column(name = "tipo_usuario", length = 20, nullable = false) // Mapeia para a coluna tipo_usuario
    private TipoUsuario tipoUsuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 30, nullable = false) // Mapeia para a coluna ROLE
    private UsuarioRole role; // Atributo do tipo enum UsuarioRole

    @CreationTimestamp // Preenche automaticamente com a data/hora da criação
    @Column(name = "dt_cadastro", updatable = false) // Mapeia para dt_cadastro, não atualizável
    private LocalDateTime dtCadastro; // Usando LocalDateTime para precisão de data e hora

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    @ToString.Exclude // Evita recursão no toString()
    private Microempreendedor microempreendedor;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    @ToString.Exclude // Evita recursão
    private Cliente cliente;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    @ToString.Exclude // Evita recursão no toString()
    private Investidor investidor;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.getRole()));
    }
    @Override
    public String getPassword() {
        return this.senha; // Retorna a senha (criptografada posteriormente)
    }

    @Override
    public String getUsername() {
        return this.email; // Usamos o email como identificador de login
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Simplificado: conta nunca expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Simplificado: conta nunca bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Simplificado: credenciais nunca expiram
    }

    @Override
    public boolean isEnabled() {
        return true; // Simplificado: conta sempre habilitada
    }
}