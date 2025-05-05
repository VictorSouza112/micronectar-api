package br.com.fiap.micronectar.controller;

import br.com.fiap.micronectar.dto.ProdutoCadastroDTO;
import br.com.fiap.micronectar.dto.ProdutoExibicaoDTO;
import br.com.fiap.micronectar.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List; // Importar List

@RestController
@RequestMapping("/api/microempreendedores/{microempreendedorId}/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    // Endpoint POST para adicionar produto
    @PostMapping
    public ResponseEntity<ProdutoExibicaoDTO> adicionarProduto(
            @PathVariable Long microempreendedorId,
            @Valid @RequestBody ProdutoCadastroDTO produtoCadastroDTO,
            @AuthenticationPrincipal UserDetails usuarioLogado
    ) {
        ProdutoExibicaoDTO produtoSalvo = produtoService.adicionarProduto(
                microempreendedorId,
                produtoCadastroDTO,
                usuarioLogado
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
    }


    // GET para listar produtos
    @GetMapping // Mapeia para GET na URL base do controller
    public ResponseEntity<List<ProdutoExibicaoDTO>> listarProdutos(
            @PathVariable Long microempreendedorId // Obtém o ID da URL
    ) {
        // Chama o serviço para buscar e converter os produtos
        List<ProdutoExibicaoDTO> produtos = produtoService.listarProdutosPorMicroempreendedor(microempreendedorId);
        // Retorna 200 OK com a lista (pode ser vazia se não houver produtos)
        return ResponseEntity.ok(produtos);
    }
}