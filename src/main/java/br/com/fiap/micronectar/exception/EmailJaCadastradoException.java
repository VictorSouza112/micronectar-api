package br.com.fiap.micronectar.exception;

public class EmailJaCadastradoException extends RuntimeException {

    public EmailJaCadastradoException(String message) {
        super(message); // Passa a mensagem para o construtor da classe pai (RuntimeException)
    }
}