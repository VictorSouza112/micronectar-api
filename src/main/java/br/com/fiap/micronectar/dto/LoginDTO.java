package br.com.fiap.micronectar.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank(message = "O e-mail é obrigatório para login")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotBlank(message = "A senha é obrigatória para login")
        // Não validamos tamanho aqui, pois a comparação será feita com o hash
        String senha
) {
}