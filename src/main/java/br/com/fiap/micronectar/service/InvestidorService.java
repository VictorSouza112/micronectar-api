package br.com.fiap.micronectar.service;

import br.com.fiap.micronectar.dto.InvestidorCadastroDTO;
import br.com.fiap.micronectar.dto.InvestidorExibicaoDTO;
import br.com.fiap.micronectar.enums.TipoUsuario;
import br.com.fiap.micronectar.enums.UsuarioRole;
import br.com.fiap.micronectar.exception.DocumentoJaCadastradoException;
import br.com.fiap.micronectar.exception.EmailJaCadastradoException;
import br.com.fiap.micronectar.model.Investidor;
import br.com.fiap.micronectar.model.Usuario;
import br.com.fiap.micronectar.repository.InvestidorRepository;
import br.com.fiap.micronectar.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails; // Importar UserDetails
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvestidorService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private InvestidorRepository investidorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public InvestidorExibicaoDTO registrarInvestidor(InvestidorCadastroDTO dto) {

        // 1. Validar duplicações
        UserDetails emailExistente = usuarioRepository.findByEmail(dto.email());
        if (emailExistente != null) {
            throw new EmailJaCadastradoException("E-mail já cadastrado: " + dto.email());
        }

        investidorRepository.findByNrDocumento(dto.nrDocumento()).ifPresent(i -> {
            throw new DocumentoJaCadastradoException("Número de documento já cadastrado: " + dto.nrDocumento());
        });

        // 2. Criar entidade Usuario
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dto.nome());
        novoUsuario.setEmail(dto.email());
        novoUsuario.setSenha(passwordEncoder.encode(dto.senha())); // Criptografa a senha
        novoUsuario.setTipoUsuario(TipoUsuario.INVESTIDOR); // Define o tipo funcional
        novoUsuario.setRole(UsuarioRole.ROLE_INVESTIDOR);   // Define o papel de segurança

        // 3. Criar entidade Investidor
        Investidor novoInvestidor = new Investidor();
        novoInvestidor.setNrDocumento(dto.nrDocumento());
        novoInvestidor.setDsPerfilInvestidor(dto.dsPerfilInvestidor());
        novoInvestidor.setDtNascimento(dto.dtNascimento());

        // 4. Associar bidirecionalmente ANTES de salvar
        novoUsuario.setInvestidor(novoInvestidor); // Associa Investidor ao Usuario
        novoInvestidor.setUsuario(novoUsuario);   // Associa Usuario ao Investidor

        // 5. Salvar o Usuario (Investidor será salvo em cascata devido ao CascadeType.ALL)
        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);

        // 6. Retornar o DTO de exibição a partir do Investidor persistido
        // Garantimos que pegamos a instância gerenciada pelo JPA após o save.
        return new InvestidorExibicaoDTO(usuarioSalvo.getInvestidor());
    }
}