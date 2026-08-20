package com.lucas.produtoscategorias.repository;

import com.lucas.produtoscategorias.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNomeIgnoreCase(String nome);
}
