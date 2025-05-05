package br.com.fiap.micronectar.repository;

import br.com.fiap.micronectar.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> { // <Entidade, Tipo da PK>

    Optional<Cliente> findByNrCpf(String nrCpf);

}