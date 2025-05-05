package br.com.fiap.micronectar.exception;

public class CpfJaCadastradoException extends RuntimeException {

    public CpfJaCadastradoException(String message) {
        super(message); // Passa a mensagem para o construtor da RuntimeException
    }
}