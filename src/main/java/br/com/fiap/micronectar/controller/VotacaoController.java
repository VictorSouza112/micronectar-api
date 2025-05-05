package br.com.fiap.micronectar.controller;

import br.com.fiap.micronectar.dto.VotoCadastroDTO;
import br.com.fiap.micronectar.dto.VotoExibicaoDTO;
import br.com.fiap.micronectar.service.VotacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Import para pegar usuário autenticado
import org.springframework.security.core.userdetails.UserDetails; // Import UserDetails
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api") // Define o path base
public class VotacaoController {

    @Autowired
    private VotacaoService votacaoService;

    @PostMapping("/microempreendedores/{id}/votos") // Mapeia para POST /api/microempreendedores/{ID_DO_ME}/votos
    public ResponseEntity<VotoExibicaoDTO> registrarVoto(
            @PathVariable("id") Long id, // Pega o {id} da URL
            @Valid @RequestBody VotoCadastroDTO dto, // Pega e valida o corpo da requisição
            @AuthenticationPrincipal UserDetails userDetails // Injeta os detalhes do usuário logado
    ) {
        // Chama o serviço passando o ID do ME, os detalhes do votador e os dados do voto
        VotoExibicaoDTO votoSalvo = votacaoService.registrarVoto(id, userDetails, dto);

        // Retorna 201 Created com o voto salvo no corpo
        return ResponseEntity.status(HttpStatus.CREATED).body(votoSalvo);
    }

}