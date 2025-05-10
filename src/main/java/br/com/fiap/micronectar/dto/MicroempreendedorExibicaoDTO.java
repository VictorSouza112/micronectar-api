package br.com.fiap.micronectar.dto;

import br.com.fiap.micronectar.enums.TipoUsuario;
import br.com.fiap.micronectar.model.Microempreendedor;

import java.time.LocalDateTime;

public record MicroempreendedorExibicaoDTO(
        // Campos comuns (replicados de UsuarioExibicaoDTO)
        Long idUsuario,
        String nomeFantasia, // Renomeado para clareza semântica neste DTO
        String email,
        TipoUsuario tipoUsuario,

        // Campos específicos do Microempreendedor
        String nrCnpj
) {

    // Construtor que recebe a entidade Microempreendedor para facilitar a conversão
    public MicroempreendedorExibicaoDTO(Microempreendedor microempreendedor) {
        this(
                microempreendedor.getIdUsuario(),
                microempreendedor.getUsuario().getNome(), // Pega o nome fantasia do Usuario associado
                microempreendedor.getUsuario().getEmail(),
                microempreendedor.getUsuario().getTipoUsuario(),
                microempreendedor.getNrCnpj() // Pega o CNPJ do Microempreendedor
        );
    }
}