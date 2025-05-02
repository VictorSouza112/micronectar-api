package br.com.fiap.micronectar.service;

import br.com.fiap.micronectar.dto.MicroempreendedorCadastroDTO;
import br.com.fiap.micronectar.dto.MicroempreendedorExibicaoDTO;
import br.com.fiap.micronectar.enums.TipoUsuario;
import br.com.fiap.micronectar.enums.UsuarioRole; // Importar UsuarioRole
import br.com.fiap.micronectar.exception.CnpjJaCadastradoException;
import br.com.fiap.micronectar.exception.EmailJaCadastradoException;
import br.com.fiap.micronectar.model.Microempreendedor;
import br.com.fiap.micronectar.model.Usuario;
import br.com.fiap.micronectar.repository.MicroempreendedorRepository;
import br.com.fiap.micronectar.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder; // Import PasswordEncoder
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MicroempreendedorService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MicroempreendedorRepository microempreendedorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public MicroempreendedorExibicaoDTO registrarMicroempreendedor(MicroempreendedorCadastroDTO dto) {

        // 1. Validar duplicações
        UserDetails usuarioExistente = usuarioRepository.findByEmail(dto.email());
        if (usuarioExistente != null) {
            throw new EmailJaCadastradoException("E-mail já cadastrado: " + dto.email());
        }
        microempreendedorRepository.findByNrCnpj(dto.nrCnpj()).ifPresent(m -> {
            throw new CnpjJaCadastradoException("CNPJ já cadastrado: " + dto.nrCnpj());
        });

        // 2. Criar a entidade Usuario
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dto.nome());
        novoUsuario.setEmail(dto.email());

        // --- ALTERADO: Criptografar a senha ---
        String senhaCriptografada = passwordEncoder.encode(dto.senha());
        novoUsuario.setSenha(senhaCriptografada); // Define a senha JÁ CRIPTOGRAFADA

        novoUsuario.setTipoUsuario(TipoUsuario.MICROEMPREENDEDOR); // Define o tipo funcional

        // --- NOVO: Definir a Role padrão ---
        novoUsuario.setRole(UsuarioRole.ROLE_MICROEMPREENDEDOR); // Define o papel para segurança

        // 3. Criar a entidade Microempreendedor
        Microempreendedor novoMicroempreendedor = new Microempreendedor();
        novoMicroempreendedor.setNrCnpj(dto.nrCnpj());
        novoMicroempreendedor.setNmFundador(dto.nmFundador());
        novoMicroempreendedor.setDsCategoria(dto.dsCategoria());
        novoMicroempreendedor.setDsHistoria(dto.dsHistoria());
        novoMicroempreendedor.setDsHistoriaFundador(dto.dsHistoriaFundador());
        novoMicroempreendedor.setPitchUrl(dto.pitchUrl());

        // 4. Associar em AMBAS as direções ANTES de salvar
        novoUsuario.setMicroempreendedor(novoMicroempreendedor);
        novoMicroempreendedor.setUsuario(novoUsuario);

        // 5. Salvar o Usuario (Microempreendedor será salvo em cascata)
        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);

        // 6. Retornar o DTO de exibição
        return new MicroempreendedorExibicaoDTO(usuarioSalvo.getMicroempreendedor());
    }
}