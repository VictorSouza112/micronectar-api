package br.com.fiap.micronectar.enums;

public enum UsuarioRole {

    // Define os papéis com a convenção ROLE_ prefix
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_MICROEMPREENDEDOR("ROLE_MICROEMPREENDEDOR"),
    ROLE_CLIENTE("ROLE_CLIENTE"),
    ROLE_INVESTIDOR("ROLE_INVESTIDOR");

    private final String role; // Campo para armazenar a String do papel

    // Construtor do enum
    UsuarioRole(String role) {
        this.role = role;
    }

    // Getter para a representação String
    public String getRole() {
        return role;
    }
}