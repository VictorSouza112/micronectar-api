package br.com.fiap.micronectar.controller;

import br.com.fiap.micronectar.config.security.TokenService;
import br.com.fiap.micronectar.dto.*; // Import geral de DTOs
import br.com.fiap.micronectar.model.Usuario;
import br.com.fiap.micronectar.service.ClienteService;
import br.com.fiap.micronectar.service.InvestidorService;
import br.com.fiap.micronectar.service.MicroempreendedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private MicroempreendedorService microempreendedorService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private InvestidorService investidorService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    // --- Endpoint de Registro de Microempreendedor ---
    @PostMapping("/register/microempreendedor")
    public ResponseEntity<MicroempreendedorExibicaoDTO> registrarMicroempreendedor(
            @Valid @RequestBody MicroempreendedorCadastroDTO microempreendedorCadastroDTO
    ) {
        MicroempreendedorExibicaoDTO microempreendedorSalvo = microempreendedorService.registrarMicroempreendedor(microempreendedorCadastroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(microempreendedorSalvo);
    }

    // --- Endpoint de Registro de Cliente ---
    @PostMapping("/register/cliente")
    public ResponseEntity<ClienteExibicaoDTO> registrarCliente(
            @Valid @RequestBody ClienteCadastroDTO clienteCadastroDTO
    ) {
        // Chama o serviço para registrar o cliente
        ClienteExibicaoDTO clienteSalvo = clienteService.registrarCliente(clienteCadastroDTO);
        // Retorna status 201 Created e o DTO de exibição do cliente
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
    }

    // --- Endpoint de Registro de Investidor ---
    @PostMapping("/register/investidor")
    public ResponseEntity<InvestidorExibicaoDTO> registrarInvestidor(
            @Valid @RequestBody InvestidorCadastroDTO investidorCadastroDTO
    ) {
        // Chama o serviço específico para registrar o investidor
        InvestidorExibicaoDTO investidorSalvo = investidorService.registrarInvestidor(investidorCadastroDTO);

        // Retorna status 201 Created e o DTO de exibição do investidor
        return ResponseEntity.status(HttpStatus.CREATED).body(investidorSalvo);
    }

    // --- Endpoint de Login ---
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO loginDto) {
        // 1. Cria o objeto de autenticação com as credenciais do DTO
        UsernamePasswordAuthenticationToken usernamePassword =
                new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.senha());

        // 2. Autentica usando o AuthenticationManager
        Authentication auth = authenticationManager.authenticate(usernamePassword);

        // 3. Se a autenticação foi bem-sucedida, gera o token JWT
        Usuario usuarioAutenticado = (Usuario) auth.getPrincipal();
        String tokenJwt = tokenService.gerarToken(usuarioAutenticado);

        // 4. Retorna o token JWT dentro de um TokenDTO na resposta 200 OK
        return ResponseEntity.ok(new TokenDTO(tokenJwt));
    }
}
