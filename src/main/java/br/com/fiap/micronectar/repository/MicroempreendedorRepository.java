package br.com.fiap.micronectar.repository;

import br.com.fiap.micronectar.model.Microempreendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MicroempreendedorRepository extends JpaRepository<Microempreendedor, Long> { // <Entidade, Tipo da PK>

    Optional<Microempreendedor> findByNrCnpj(String nrCnpj);

}