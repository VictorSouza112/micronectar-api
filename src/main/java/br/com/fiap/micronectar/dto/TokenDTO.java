package br.com.fiap.micronectar.dto;

// DTO simples para encapsular o token JWT retornado no login.
public record TokenDTO(String token) {
}