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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints Públicos
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/register/**").permitAll()
                        // Endpoint de Adicionar Produtos
                        .requestMatchers(HttpMethod.POST, "/api/microempreendedores/*/produtos").hasAnyRole("MICROEMPREENDEDOR", "ADMIN")
                        // Permite POST em /api/microempreendedores/{id}/votos apenas para CLIENTE, INVESTIDOR ou ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/microempreendedores/*/votos").hasAnyRole("CLIENTE", "INVESTIDOR", "ADMIN")
                        // Regra Geral: Qualquer outra requisição exige autenticação
                        .anyRequest().authenticated()
                )
                .addFilterBefore(verificarToken, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}