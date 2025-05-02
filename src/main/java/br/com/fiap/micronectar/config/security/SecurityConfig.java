package br.com.fiap.micronectar.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Marca como uma classe de configuração Spring
@EnableWebSecurity // Habilita a configuração de segurança web do Spring Security
public class SecurityConfig {

    // Injetar o filtro customizado ---
    @Autowired
    private VerificarToken verificarToken;

    @Bean // Expõe o resultado deste método como um Bean gerenciado pelo Spring
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // Expõe o AuthenticationManager como um Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean // Expõe o SecurityFilterChain como um Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // 1. Desabilitar CSRF (Cross-Site Request Forgery), pois usaremos autenticação stateless (JWT)
                .csrf(csrf -> csrf.disable())

                // 2. Configurar o gerenciamento de sessão como STATELESS
                // A aplicação não criará nem usará sessões HTTP; cada requisição deve ser auto-contida (com token)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Configurar regras de autorização para as requisições HTTP
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso público (sem autenticação) para requisições POST em /api/auth/login
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        // Permite acesso público para POST em /api/auth/register/microempreendedor (e futuros /cliente, /investidor)
                        .requestMatchers(HttpMethod.POST, "/api/auth/register/**").permitAll()
                        // Qualquer outra requisição (anyRequest) deve ser autenticada (authenticated)
                        .anyRequest().authenticated()
                )

                // 4. Constrói e retorna o SecurityFilterChain
                .addFilterBefore(verificarToken, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}