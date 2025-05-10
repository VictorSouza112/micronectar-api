package br.com.fiap.micronectar.dto;

import br.com.fiap.micronectar.enums.TipoUsuario;
import br.com.fiap.micronectar.model.Usuario; // Importar o modelo Usuario

import java.time.LocalDateTime;

public record UsuarioExibicaoDTO(
        Long idUsuario,
        String nome, // Nome do usuário ou nome fantasia da empresa
        String email,
        TipoUsuario tipoUsuario
) {

    // Construtor que recebe a entidade Usuario genérica
    public UsuarioExibicaoDTO(Usuario usuario) {
        this(
                usuario.getIdUsuario(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipoUsuario()
        );
    }
}