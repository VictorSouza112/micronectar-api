package br.com.fiap.micronectar.service;

import br.com.fiap.micronectar.dto.ClienteCadastroDTO;
import br.com.fiap.micronectar.dto.ClienteExibicaoDTO;
import br.com.fiap.micronectar.enums.TipoUsuario;
import br.com.fiap.micronectar.enums.UsuarioRole;
import br.com.fiap.micronectar.exception.CpfJaCadastradoException;
import br.com.fiap.micronectar.exception.EmailJaCadastradoException;
import br.com.fiap.micronectar.model.Cliente;
import br.com.fiap.micronectar.model.Usuario;
import br.com.fiap.micronectar.repository.ClienteRepository;
import br.com.fiap.micronectar.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails; // Import para UserDetails
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public ClienteExibicaoDTO registrarCliente(ClienteCadastroDTO dto) {

        // 1. Validar duplicações (existente)
        UserDetails emailExistente = usuarioRepository.findByEmail(dto.email());
        if (emailExistente != null) {
            throw new EmailJaCadastradoException("E-mail já cadastrado: " + dto.email());
        }
        clienteRepository.findByNrCpf(dto.nrCpf()).ifPresent(c -> {
            throw new CpfJaCadastradoException("CPF já cadastrado: " + dto.nrCpf());
        });

        // 2. Criar entidade Usuario (existente)
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dto.nome());
        novoUsuario.setEmail(dto.email());
        novoUsuario.setSenha(passwordEncoder.encode(dto.senha()));
        novoUsuario.setTipoUsuario(TipoUsuario.CLIENTE);
        novoUsuario.setRole(UsuarioRole.ROLE_CLIENTE);


        // 3. Criar entidade Cliente (existente)
        Cliente novoCliente = new Cliente();
        novoCliente.setNrCpf(dto.nrCpf());
        novoCliente.setDtNascimento(dto.dtNascimento());

        // 4. Associar as entidades em ambas as direções (existente)
        novoUsuario.setCliente(novoCliente);
        novoCliente.setUsuario(novoUsuario);

        // 5. Salvar o Usuario (Cliente será salvo em cascata) (existente)
        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);

        // --- ALTERADO: Re-buscar o usuário salvo para garantir dados atualizados ---
        // Forçar a busca do banco após o save garante que temos o estado mais recente,
        // incluindo valores gerados como @CreationTimestamp.
        Usuario usuarioCompleto = usuarioRepository.findById(usuarioSalvo.getIdUsuario())
                .orElseThrow(() -> new EntityNotFoundException("Erro ao buscar usuário recém-criado com ID: " + usuarioSalvo.getIdUsuario())); // Lança exceção se não encontrar (improvável aqui)

        // Acessa o cliente associado a partir do usuário completo re-buscado
        Cliente clienteCompleto = usuarioCompleto.getCliente();

        // 6. Retornar o DTO de exibição usando os dados re-buscados
        return new ClienteExibicaoDTO(
                usuarioCompleto.getIdUsuario(),
                usuarioCompleto.getNome(),
                usuarioCompleto.getEmail(),
                usuarioCompleto.getTipoUsuario(),
                clienteCompleto.getNrCpf(),
                clienteCompleto.getDtNascimento()
        );
    }
}