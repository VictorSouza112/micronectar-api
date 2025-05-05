package br.com.fiap.micronectar.repository;

import br.com.fiap.micronectar.model.Votacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotacaoRepository extends JpaRepository<Votacao, Long> { // <Entidade, Tipo da PK>

    Optional<Votacao> findByVotadorIdUsuarioAndMicroempreendedorVotadoIdUsuario(Long votadorId, Long microempreendedorId);

}