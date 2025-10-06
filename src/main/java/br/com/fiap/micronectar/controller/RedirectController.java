package br.com.fiap.micronectar.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Controlador REST para redirecionar a URL raiz para a documentação do Swagger UI.
 */
@RestController
@RequestMapping("/")
@Hidden // Anotação do Swagger (OpenAPI 3) para ocultar todos os endpoints deste controller da documentação
public class RedirectController {

    /**
     * Redireciona requisições GET da raiz ("/") para a página principal do Swagger UI.
     * @param response O objeto de resposta HTTP, usado para executar o redirecionamento.
     * @throws IOException Se ocorrer um erro de I/O durante o redirecionamento.
     */
    @GetMapping
    public void redirectToSwaggerUi(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui/index.html");
    }
}