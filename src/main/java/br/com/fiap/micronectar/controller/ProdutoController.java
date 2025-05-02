package br.com.fiap.micronectar.controller;

import br.com.fiap.micronectar.dto.ProdutoCadastroDTO;
import br.com.fiap.micronectar.dto.ProdutoExibicaoDTO;
import br.com.fiap.micronectar.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Para injetar usuário logado
import org.springframework.security.core.userdetails.UserDetails; // Tipo do usuário logado
import org.springframework.web.bind.annotation.*;

@RestController
// O RequestMapping base inclui o path variable para o ID do microempreendedor
@RequestMapping("/api/microempreendedores/{microempreendedorId}/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping // Mapeia para POST na URL base do controller
    public ResponseEntity<ProdutoExibicaoDTO> adicionarProduto(
            @PathVariable Long microempreendedorId, // Obtém o ID da URL
            @Valid @RequestBody ProdutoCadastroDTO produtoCadastroDTO, // Obtém e valida o corpo
            @AuthenticationPrincipal UserDetails usuarioLogado // Injeta os detalhes do usuário autenticado
    ) {
        ProdutoExibicaoDTO produtoSalvo = produtoService.adicionarProduto(
                microempreendedorId,
                produtoCadastroDTO,
                usuarioLogado
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
    }
}