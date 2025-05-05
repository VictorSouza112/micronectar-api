package br.com.fiap.micronectar.advice;

import br.com.fiap.micronectar.exception.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
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

    // Handler para erros de validação (@Valid) - existente
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        fieldErrors.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    // Handler para Email duplicado - existente
    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<Map<String, String>> handleEmailJaCadastradoException(EmailJaCadastradoException ex) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody);
    }

    // Handler para CNPJ duplicado - existente
    @ExceptionHandler(CnpjJaCadastradoException.class)
    public ResponseEntity<Map<String, String>> handleCnpjJaCadastradoException(CnpjJaCadastradoException ex) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody);
    }

    // --- Handler para CPF duplicado ---
    @ExceptionHandler(CpfJaCadastradoException.class)
    public ResponseEntity<Map<String, String>> handleCpfJaCadastradoException(CpfJaCadastradoException ex) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("erro", ex.getMessage()); // Adiciona a mensagem específica do CPF
        // Retorna ResponseEntity com o corpo e o status 409 Conflict
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody);
    }

    @ExceptionHandler(DocumentoJaCadastradoException.class)
    public ResponseEntity<Map<String, String>> handleDocumentoJaCadastradoException(DocumentoJaCadastradoException ex) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("erro", ex.getMessage()); // Pega a mensagem da exceção
        // Retorna ResponseEntity com o corpo e o status 409 Conflict
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody);
    }

    // --- Handler para VotoDuplicadoException ---
    @ExceptionHandler(VotoDuplicadoException.class)
    public ResponseEntity<Map<String, String>> handleVotoDuplicadoException(VotoDuplicadoException ex) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody);
    }

    // --- Handler para VotoInvalidoException ---
    @ExceptionHandler(VotoInvalidoException.class)
    public ResponseEntity<Map<String, String>> handleVotoInvalidoException(VotoInvalidoException ex) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorBody);
    }

    // --- Handler para EntityNotFoundException ---
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("erro", ex.getMessage()); // A mensagem geralmente já diz qual entidade não foi encontrada
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
    }

    // --- Handler para DataIntegrityViolationException ---
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Map<String, String> errorBody = new HashMap<>();
        // Mensagem genérica, pois pode haver várias causas. Poderia analisar ex.getMessage() para mais detalhes.
        errorBody.put("erro", "Violação de integridade de dados. Verifique se o recurso já existe ou se os dados são válidos.");
        // Logar a exceção original é importante para debug no servidor
        // log.error("DataIntegrityViolationException: ", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody);
    }
}