package br.com.fiap.micronectar.dto;

import br.com.fiap.micronectar.model.Produto; // Importar entidade
import java.math.BigDecimal;


public record ProdutoExibicaoDTO(
        Long idProduto,
        String nmProduto,
        BigDecimal vlProduto,
        Integer qtProduto,
        String dsProduto,
        String fotoUrl,
        Long idMicroempreendedor // Inclui o ID do ME dono do produto
) {

    public ProdutoExibicaoDTO(Produto produto) {
        this(
                produto.getIdProduto(),
                produto.getNmProduto(),
                produto.getVlProduto(),
                produto.getQtProduto(),
                produto.getDsProduto(),
                produto.getFotoUrl(),
                // Acessa o ID do microempreendedor através da associação na entidade Produto
                produto.getMicroempreendedor() != null ? produto.getMicroempreendedor().getIdUsuario() : null
        );
    }
}