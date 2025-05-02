package br.com.fiap.micronectar.config.security;

import br.com.fiap.micronectar.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Marca como um componente Spring, permitindo a injeção de dependências
public class VerificarToken extends OncePerRequestFilter { // Garante que o filtro execute apenas uma vez por requisição

    @Autowired
    private TokenService tokenService; // Serviço para validar o token

    @Autowired
    private UsuarioRepository usuarioRepository; // Repositório para buscar o usuário

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Recuperar o token do cabeçalho Authorization
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;

        // 2. Verificar se o cabeçalho existe e começa com "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // 3. Extrair apenas o token (removendo "Bearer ")
            token = authorizationHeader.substring(7); // "Bearer ".length() == 7
        }

        // 4. Validar o token se ele foi extraído
        if (token != null) {
            String subject = tokenService.validarToken(token);

            // 5. Se o token for válido (subject não é null)
            if (subject != null) {
                // 6. Carregar os detalhes do usuário (UserDetails) do banco usando o email (subject)
                UserDetails usuarioDetails = usuarioRepository.findByEmail(subject);

                // Verificar se o usuário realmente existe no banco
                if (usuarioDetails != null) {
                    // 7. Criar um objeto de autenticação para o Spring Security
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    usuarioDetails,
                                    null, // Credenciais não são necessárias aqui
                                    usuarioDetails.getAuthorities());

                    // 8. Definir o usuário como autenticado no contexto de segurança do Spring
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        // 9. Continuar a execução da cadeia de filtros
        filterChain.doFilter(request, response);
    }
}