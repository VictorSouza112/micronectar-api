package br.com.fiap.micronectar.exception;

public class VotoDuplicadoException extends RuntimeException {

  public VotoDuplicadoException(String message) {
    super(message); // Passa a mensagem para o construtor da classe pai
  }
}