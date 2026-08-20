package com.lucas.produtoscategorias.config;

import com.lucas.produtoscategorias.entity.Categoria;
import com.lucas.produtoscategorias.entity.Produto;
import com.lucas.produtoscategorias.repository.CategoriaRepository;
import com.lucas.produtoscategorias.repository.ProdutoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class CargaInicial {

    @Bean
    CommandLineRunner carregarDados(
            CategoriaRepository categoriaRepository,
            ProdutoRepository produtoRepository
    ) {
        return args -> {
            if (categoriaRepository.count() > 0) {
                return;
            }

            Categoria perifericos = categoriaRepository.save(
                    new Categoria("Periféricos", "Acessórios para computadores")
            );

            Categoria computadores = categoriaRepository.save(
                    new Categoria("Computadores", "Notebooks e desktops")
            );

            produtoRepository.saveAll(List.of(
                    new Produto("Teclado", "Teclado mecânico", new BigDecimal("249.90"), perifericos),
                    new Produto("Mouse", "Mouse gamer", new BigDecimal("129.90"), perifericos),
                    new Produto("Teclado Gamer", "Teclado RGB", new BigDecimal("399.90"), perifericos),
                    new Produto("Notebook", "Notebook para desenvolvimento", new BigDecimal("4599.90"), computadores),
                    new Produto("Desktop", "Computador desktop", new BigDecimal("3899.90"), computadores)
            ));
        };
    }
}
