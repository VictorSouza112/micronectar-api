package br.com.fiap.micronectar.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VotoCadastroDTO(
        // O voto "promissor" é obrigatório
        @NotNull(message = "A indicação 'promissor' (true/false) é obrigatória")
        Boolean promissor,

        // O comentário é opcional, mas se presente, tem um limite de tamanho
        @Size(max = 500, message = "Comentário não pode exceder 500 caracteres")
        String comentario // Opcional
) {
}