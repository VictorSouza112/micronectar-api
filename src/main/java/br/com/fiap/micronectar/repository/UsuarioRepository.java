package br.com.fiap.micronectar.repository;

import br.com.fiap.micronectar.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository // Indica que é um componente de repositório do Spring (gerenciado pelo container)
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    UserDetails findByEmail(String email); // <- Alterado o tipo de retorno

}