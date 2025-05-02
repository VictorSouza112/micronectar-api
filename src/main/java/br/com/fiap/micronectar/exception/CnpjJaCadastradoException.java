package br.com.fiap.micronectar.exception;

public class CnpjJaCadastradoException extends RuntimeException {

    public CnpjJaCadastradoException(String message) {
        super(message); // Passa a mensagem para o construtor da classe pai
    }
}