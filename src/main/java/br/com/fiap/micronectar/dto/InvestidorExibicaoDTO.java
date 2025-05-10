package br.com.fiap.micronectar.dto;

import br.com.fiap.micronectar.enums.TipoUsuario;
import br.com.fiap.micronectar.model.Investidor; // Importar entidade Investidor

import java.time.LocalDate;
import java.time.LocalDateTime;

public record InvestidorExibicaoDTO(
        // Campos do Usuario associado
        Long idUsuario,
        String nome,
        String email,
        TipoUsuario tipoUsuario,

        // Campos do Investidor
        String nrDocumento,
        String dsPerfilInvestidor,
        LocalDate dtNascimento // Incluindo a data de nascimento/fundação
) {

    // Construtor que recebe a entidade Investidor para facilitar a conversão
    public InvestidorExibicaoDTO(Investidor investidor) {
        this(
                investidor.getIdUsuario(), // ou investidor.getUsuario().getIdUsuario()
                investidor.getUsuario().getNome(),
                investidor.getUsuario().getEmail(),
                investidor.getUsuario().getTipoUsuario(),
                investidor.getNrDocumento(),
                investidor.getDsPerfilInvestidor(),
                investidor.getDtNascimento()
        );
    }
}