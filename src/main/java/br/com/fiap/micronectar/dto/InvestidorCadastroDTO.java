package br.com.fiap.micronectar.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record InvestidorCadastroDTO(
        // Dados para a entidade Usuario
        @NotBlank(message = "O nome do investidor é obrigatório")
        @Size(max = 150, message = "Nome não pode exceder 150 caracteres")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        @Size(max = 100, message = "E-mail não pode exceder 100 caracteres")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, max = 20, message = "Senha deve ter entre 6 e 20 caracteres")
        String senha,

        // Dados para a entidade Investidor
        @NotBlank(message = "O número do documento (CPF/CNPJ) é obrigatório")
        @Size(max = 20, message = "Número do documento não pode exceder 20 caracteres")
        String nrDocumento,

        @Size(max = 200, message = "Descrição do perfil não pode exceder 200 caracteres")
        String dsPerfilInvestidor, // Opcional

        @PastOrPresent(message = "Data de nascimento/fundação não pode ser no futuro")
        LocalDate dtNascimento // Opcional
) {
}