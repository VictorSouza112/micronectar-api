package br.com.fiap.micronectar.dto;

import jakarta.validation.constraints.*; // Import geral para constraints
import org.hibernate.validator.constraints.br.CPF; // Import específico para @CPF

import java.time.LocalDate;

public record ClienteCadastroDTO(

        // Dados que irão para a entidade Usuario
        @NotBlank(message = "O nome do cliente é obrigatório")
        @Size(max = 150, message = "Nome do cliente não pode exceder 150 caracteres")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        @Size(max = 100, message = "E-mail não pode exceder 100 caracteres")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, max = 20, message = "Senha deve ter entre 6 e 20 caracteres")
        String senha,

        // Dados que irão para a entidade Cliente
        @NotBlank(message = "O CPF é obrigatório")
        @CPF(message = "CPF inválido") // Validação específica para CPF brasileiro (Hibernate Validator)
        String nrCpf,

        @NotNull(message = "A data de nascimento é obrigatória")
        @Past(message = "Data de nascimento deve ser uma data no passado") // Garante que a data não é futura
        LocalDate dtNascimento // Tipo LocalDate para data

) {
}