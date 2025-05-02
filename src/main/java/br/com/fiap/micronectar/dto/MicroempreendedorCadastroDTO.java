package br.com.fiap.micronectar.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Usamos 'record' para um DTO imutável e conciso
public record MicroempreendedorCadastroDTO(

        // Dados que irão para a entidade Usuario
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 150, message = "Nome não pode exceder 150 caracteres")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        @Size(max = 100, message = "E-mail não pode exceder 100 caracteres")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, max = 20, message = "Senha deve ter entre 6 e 20 caracteres") // Ajuste os limites se necessário
        String senha,

        // Dados que irão para a entidade Microempreendedor
        @NotBlank(message = "O CNPJ é obrigatório")
        // Exemplo de regex simples para formato CNPJ (XX.XXX.XXX/XXXX-XX)
        // Para validação real (dígitos verificadores), seria necessária uma biblioteca ou lógica customizada
        @Pattern(regexp = "^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$", message = "Formato de CNPJ inválido (XX.XXX.XXX/XXXX-XX)")
        String nrCnpj,

        @Size(max = 150, message = "Nome do fundador não pode exceder 150 caracteres")
        String nmFundador, // Opcional

        @Size(max = 100, message = "Descrição da categoria não pode exceder 100 caracteres")
        String dsCategoria, // Opcional

        String dsHistoria, // CLOB, sem limite de tamanho aqui, mas pode ser adicionado se necessário

        String dsHistoriaFundador, // CLOB

        @Size(max = 500, message = "URL do Pitch não pode exceder 500 caracteres")
        String pitchUrl // Opcional

) {
}