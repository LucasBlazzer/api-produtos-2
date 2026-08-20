package com.lucas.produtoscategorias.repository;

import com.lucas.produtoscategorias.entity.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    /*
     * Consulta JPQL personalizada solicitada no exercício.
     * Exemplo: nome = "Teclado".
     */
    @Query("SELECT p FROM Produto p WHERE LOWER(p.nome) = LOWER(:nome)")
    Page<Produto> buscarPorNomeExato(
            @Param("nome") String nome,
            Pageable pageable
    );

    /* Query Method Naming para filtro parcial, ignorando maiúsculas/minúsculas. */
    Page<Produto> findByNomeContainingIgnoreCase(
            String nome,
            Pageable pageable
    );
}
