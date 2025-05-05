package br.com.fiap.micronectar.dto;

import br.com.fiap.micronectar.model.Votacao; // Importar entidade Votacao

import java.time.LocalDateTime;

public record VotoExibicaoDTO(
        Long idVotacao,
        Long idVotador,
        // String nomeVotador, // Poderíamos adicionar se quiséssemos, mas complica o construtor
        Long idMicroempreendedor,
        LocalDateTime dtVotacao,
        Boolean promissor,
        String comentario
) {

    // Construtor que recebe a entidade Votacao para facilitar a conversão
    public VotoExibicaoDTO(Votacao votacao) {
        this(
                votacao.getIdVotacao(),
                votacao.getVotador().getIdUsuario(), // Pega o ID do Usuario votador associado
                votacao.getMicroempreendedorVotado().getIdUsuario(), // Pega o ID do ME votado associado
                votacao.getDtVotacao(),
                votacao.getPromissor(),
                votacao.getComentario()
        );
    }
}