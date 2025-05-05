package br.com.fiap.micronectar.repository;

import br.com.fiap.micronectar.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Importar List

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByMicroempreendedorIdUsuario(Long idUsuario);
}