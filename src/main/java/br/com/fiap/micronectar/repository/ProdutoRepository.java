package br.com.fiap.micronectar.repository;

import br.com.fiap.micronectar.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Indica que é um componente de repositório Spring
public interface ProdutoRepository extends JpaRepository<Produto, Long> { // <Entidade, Tipo da PK>

}