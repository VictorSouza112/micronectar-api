package br.com.fiap.micronectar.advice;

import br.com.fiap.micronectar.exception.CnpjJaCadastradoException;
import br.com.fiap.micronectar.exception.EmailJaCadastradoException;
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

@RestControllerAdvice // Indica que esta classe interceptará exceções de RestControllers
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Define o status padrão para este handler
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        // Obtém todos os erros de campo da exceção
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        // Itera sobre os erros e adiciona ao mapa (nome_do_campo -> mensagem_de_erro)
        fieldErrors.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<Map<String, String>> handleEmailJaCadastradoException(EmailJaCadastradoException ex) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("erro", ex.getMessage()); // Adiciona a mensagem da exceção ao corpo
        // Retorna ResponseEntity com o corpo e o status 409 Conflict
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody);
    }

    @ExceptionHandler(CnpjJaCadastradoException.class)
    public ResponseEntity<Map<String, String>> handleCnpjJaCadastradoException(CnpjJaCadastradoException ex) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("erro", ex.getMessage()); // Adiciona a mensagem da exceção ao corpo
        // Retorna ResponseEntity com o corpo e o status 409 Conflict
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody);
    }

}