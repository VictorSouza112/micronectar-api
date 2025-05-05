package br.com.fiap.micronectar.service;

import br.com.fiap.micronectar.dto.VotoCadastroDTO;
import br.com.fiap.micronectar.dto.VotoExibicaoDTO;
import br.com.fiap.micronectar.exception.VotoDuplicadoException;
import br.com.fiap.micronectar.exception.VotoInvalidoException;
import br.com.fiap.micronectar.model.Microempreendedor;
import br.com.fiap.micronectar.model.Usuario;
import br.com.fiap.micronectar.model.Votacao;
import br.com.fiap.micronectar.repository.MicroempreendedorRepository;
import br.com.fiap.micronectar.repository.UsuarioRepository;
import br.com.fiap.micronectar.repository.VotacaoRepository;
import jakarta.persistence.EntityNotFoundException; // Import padrão para recurso não encontrado
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VotacaoService {

    @Autowired
    private VotacaoRepository votacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MicroempreendedorRepository microempreendedorRepository;

    @Transactional
    public VotoExibicaoDTO registrarVoto(Long idMicroempreendedor, UserDetails votadorDetails, VotoCadastroDTO dto) {

        // 1. Buscar o Microempreendedor que receberá o voto
        Microempreendedor meVotado = microempreendedorRepository.findById(idMicroempreendedor)
                .orElseThrow(() -> new EntityNotFoundException("Microempreendedor não encontrado com ID: " + idMicroempreendedor));

        // 2. Buscar o Usuário (votador) que está fazendo o voto
        // Usamos o username (email) do UserDetails fornecido pelo Spring Security
        Usuario votador = (Usuario) usuarioRepository.findByEmail(votadorDetails.getUsername());
        if (votador == null) {
            // Isso não deveria acontecer se a autenticação passou, mas é uma salvaguarda
            throw new EntityNotFoundException("Usuário votador não encontrado: " + votadorDetails.getUsername());
        }

        // 3. Validação: ME não pode votar em si mesmo
        if (votador.getIdUsuario().equals(meVotado.getIdUsuario())) {
            throw new VotoInvalidoException("Microempreendedor não pode votar em si mesmo.");
        }

        // 4. Validação: Verificar se já existe um voto deste usuário para este ME
        votacaoRepository.findByVotadorIdUsuarioAndMicroempreendedorVotadoIdUsuario(
                votador.getIdUsuario(), meVotado.getIdUsuario()
        ).ifPresent(v -> {
            throw new VotoDuplicadoException("Usuário já votou neste microempreendedor.");
        });

        // 5. Criar a nova entidade Votacao
        Votacao novaVotacao = new Votacao();
        novaVotacao.setVotador(votador);
        novaVotacao.setMicroempreendedorVotado(meVotado);
        novaVotacao.setPromissor(dto.promissor());
        novaVotacao.setComentario(dto.comentario());
        // dtVotacao será preenchido automaticamente pelo @CreationTimestamp

        // 6. Salvar o voto
        Votacao votacaoSalva = votacaoRepository.save(novaVotacao);

        // 7. Atualizar o contador de votos "promissor" no Microempreendedor, se aplicável
        if (votacaoSalva.getPromissor()) {
            // Incrementa o contador na entidade gerenciada (meVotado)
            meVotado.setQtVotosPromissor(meVotado.getQtVotosPromissor() + 1);
            // Salva a entidade ME atualizada. Como já está gerenciada, o save funciona como merge.
            microempreendedorRepository.save(meVotado);
        }

        // 8. Retornar o DTO de exibição do voto
        return new VotoExibicaoDTO(votacaoSalva);
    }
}