package br.com.fiap.micronectar.dto;

import jakarta.validation.constraints.*; // Importar todas as constraints
import java.math.BigDecimal;

public record ProdutoCadastroDTO(

        @NotBlank(message = "Nome do produto/serviço é obrigatório")
        @Size(max = 150, message = "Nome não pode exceder 150 caracteres")
        String nmProduto,

        @NotNull(message = "Valor do produto/serviço é obrigatório")
        @Positive(message = "Valor deve ser positivo")
        // Poderia adicionar @Digits(integer=8, fraction=2) para limitar tamanho total
        BigDecimal vlProduto,

        // Quantidade é opcional (pode ser nulo para serviços),
        // mas se fornecida, deve ser não negativa.
        @Min(value = 0, message = "Quantidade não pode ser negativa")
        Integer qtProduto,

        @Size(max = 1000, message = "Descrição não pode exceder 1000 caracteres")
        String dsProduto, // Opcional

        @Size(max = 500, message = "URL da foto não pode exceder 500 caracteres")
        // Poderia adicionar @URL do Hibernate Validator se quisesse validar formato URL
        String fotoUrl // Opcional

) {
}