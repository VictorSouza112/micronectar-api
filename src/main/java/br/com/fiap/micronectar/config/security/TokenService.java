package br.com.fiap.micronectar.config.security;

import br.com.fiap.micronectar.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // Injeta o valor da propriedade api.security.token.secret do application.properties
    @Value("${api.security.token.secret}")
    private String palavraSecreta;

    // Define um nome constante para o emissor do token (identifica quem gerou)
    private static final String ISSUER = "Micronectar API";

    public String gerarToken(Usuario usuario) {
        try {
            // Define o algoritmo de assinatura (HMAC256) usando a chave secreta
            Algorithm algorithm = Algorithm.HMAC256(palavraSecreta);

            // Cria e assina o token JWT
            String token = JWT.create()
                    .withIssuer(ISSUER) // Define o emissor
                    .withSubject(usuario.getEmail()) // Define o "assunto" (identificador do usuário - usamos email)
                    .withExpiresAt(gerarDataDeExpiracao()) // Define o tempo de expiração
                    // Você pode adicionar outras claims (informações) aqui se necessário:
                    // .withClaim("idUsuario", usuario.getIdUsuario())
                    // .withClaim("nome", usuario.getNome())
                    // .withClaim("role", usuario.getRole().name()) // Exemplo de claim para role
                    .sign(algorithm); // Assina o token com o algoritmo
            return token;
        } catch (JWTCreationException exception) {
            // Lança uma exceção em tempo de execução se a criação do token falhar
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String validarToken(String tokenJWT) {
        try {
            // Define o mesmo algoritmo de assinatura usado na geração
            Algorithm algorithm = Algorithm.HMAC256(palavraSecreta);

            // Configura o verificador exigindo o mesmo algoritmo e emissor
            return JWT.require(algorithm)
                    .withIssuer(ISSUER) // Garante que o emissor é o esperado
                    .build() // Constrói o verificador
                    .verify(tokenJWT) // Tenta verificar o token (lança exceção se inválido)
                    .getSubject(); // Retorna o subject (email) se a verificação for bem-sucedida
        } catch (JWTVerificationException exception) {
            // Retorna null (ou vazio) se o token for inválido (expirado, assinatura incorreta, etc.)
            // Não logue a exceção aqui em produção para não poluir logs com tokens inválidos de propósito
            return null;
        }
    }

    private Instant gerarDataDeExpiracao() {
        // Define a expiração para 2 horas a partir do momento atual
        // Usa o fuso horário de São Paulo (-03:00) como referência
        return LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}