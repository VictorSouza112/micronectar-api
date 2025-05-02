package br.com.fiap.micronectar.service;

import br.com.fiap.micronectar.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // Marca como um serviço gerenciado pelo Spring
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository; // Injeta o repositório de usuários

    // Método chamado pelo Spring Security para carregar os detalhes do usuário
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Usa o método findByEmail do repositório para buscar o usuário
        // Spring Data JPA consegue retornar UserDetails diretamente porque Usuario implementa a interface.
        UserDetails userDetails = usuarioRepository.findByEmail(username);

        // Verifica se o usuário foi encontrado
        if (userDetails == null) {
            // Lança a exceção padrão do Spring Security se não encontrar
            throw new UsernameNotFoundException("Usuário não encontrado com o email: " + username);
        }

        // Retorna os detalhes do usuário encontrados
        return userDetails;
    }
}