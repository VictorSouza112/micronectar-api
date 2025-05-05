package br.com.fiap.micronectar.exception;

public class DocumentoJaCadastradoException extends RuntimeException {

  public DocumentoJaCadastradoException(String message) {
    super(message); // Passa a mensagem para o construtor da classe pai (RuntimeException)
  }
}