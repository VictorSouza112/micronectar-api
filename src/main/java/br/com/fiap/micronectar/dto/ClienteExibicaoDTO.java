package br.com.fiap.micronectar.dto;

import br.com.fiap.micronectar.enums.TipoUsuario;
import br.com.fiap.micronectar.model.Cliente; // Importar entidade Cliente

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ClienteExibicaoDTO(
        // Campos comuns (de Usuario)
        Long idUsuario,
        String nome,
        String email,
        TipoUsuario tipoUsuario,
        LocalDateTime dtCadastro,

        // Campos específicos do Cliente
        String nrCpf,
        LocalDate dtNascimento
) {

    public ClienteExibicaoDTO(Cliente cliente) {
        this(
                cliente.getIdUsuario(), // Ou cliente.getUsuario().getIdUsuario()
                cliente.getUsuario().getNome(), // Pega nome do Usuario associado
                cliente.getUsuario().getEmail(), // Pega email do Usuario associado
                cliente.getUsuario().getTipoUsuario(), // Pega tipo do Usuario associado
                cliente.getUsuario().getDtCadastro(), // Pega data cadastro do Usuario associado
                cliente.getNrCpf(), // Pega CPF do Cliente
                cliente.getDtNascimento() // Pega data nascimento do Cliente
        );
    }
}