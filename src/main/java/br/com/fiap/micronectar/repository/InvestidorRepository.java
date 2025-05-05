package br.com.fiap.micronectar.repository;

import br.com.fiap.micronectar.model.Investidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestidorRepository extends JpaRepository<Investidor, Long> { // <Entidade, Tipo da PK>

    Optional<Investidor> findByNrDocumento(String nrDocumento);

}