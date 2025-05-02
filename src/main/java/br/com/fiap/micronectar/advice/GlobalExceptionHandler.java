package br.com.fiap.micronectar.advice;

import br.com.fiap.micronectar.exception.AcessoNegadoException; // Importar
import br.com.fiap.micronectar.exception.CnpjJaCadastradoException;
import br.com.fiap.micronectar.exception.EmailJaCadastradoException;
import br.com.fiap.micronectar.exception.MicroempreendedorNotFoundException; // Importar
import org.springframework.dao.DataIntegrityViolationException; // Importar para tratar erros UNIQUE do banco
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handler para Bean Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        fieldErrors.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    // Handler para Email Duplicado
    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<Map<String, String>> handleEmailJaCadastradoException(EmailJaCadastradoException ex) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody);
    }

    // Handler para CNPJ Duplicado
    @ExceptionHandler(CnpjJaCadastradoException.class)
    public ResponseEntity<Map<String, String>> handleCnpjJaCadastradoException(CnpjJaCadastradoException ex) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody);
    }

    // Handler para Microempreendedor Não Encontrado
    @ExceptionHandler(MicroempreendedorNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleMicroempreendedorNotFoundException(MicroempreendedorNotFoundException ex) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("erro", ex.getMessage());
        // Retorna 404 Not Found
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
    }

    // Handler para Acesso Negado (Regra de Negócio)
    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<Map<String, String>> handleAcessoNegadoException(AcessoNegadoException ex) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("erro", ex.getMessage());
        // Retorna 403 Forbidden
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorBody);
    }
}