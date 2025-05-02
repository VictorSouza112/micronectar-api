package br.com.fiap.micronectar.controller;

import br.com.fiap.micronectar.config.security.TokenService; // Importar TokenService
import br.com.fiap.micronectar.dto.LoginDTO;
import br.com.fiap.micronectar.dto.MicroempreendedorCadastroDTO;
import br.com.fiap.micronectar.dto.MicroempreendedorExibicaoDTO;
import br.com.fiap.micronectar.dto.TokenDTO; // Importar TokenDTO
import br.com.fiap.micronectar.model.Usuario; // Importar Usuario
import br.com.fiap.micronectar.service.MicroempreendedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication; // Importar Authentication
// UsernamePasswordAuthenticationToken já está importado implicitamente ou explicitamente
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private MicroempreendedorService microempreendedorService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/register/microempreendedor")
    public ResponseEntity<MicroempreendedorExibicaoDTO> registrarMicroempreendedor(
            @Valid @RequestBody MicroempreendedorCadastroDTO microempreendedorCadastroDTO
    ) {
        MicroempreendedorExibicaoDTO microempreendedorSalvo = microempreendedorService.registrarMicroempreendedor(microempreendedorCadastroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(microempreendedorSalvo);
    }

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