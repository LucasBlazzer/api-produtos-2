package com.lucas.api_produtos.controller;

import com.lucas.api_produtos.model.Produto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final List<Produto> produtos =
            new ArrayList<>();

    private int proximoId = 1;

    public ProdutoController() {

        produtos.add(
                new Produto(
                        proximoId++,
                        "Notebook",
                        3500.00
                )
        );

        produtos.add(
                new Produto(
                        proximoId++,
                        "Mouse",
                        89.90
                )
        );

        produtos.add(
                new Produto(
                        proximoId++,
                        "Teclado",
                        249.90
                )
        );
    }

    // GET /produtos
    @GetMapping
    public List<Produto> listarTodos() {

        return produtos;
    }

    // GET /produtos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(
            @PathVariable Integer id
    ) {

        Optional<Produto> produtoEncontrado =
                produtos.stream()
                        .filter(
                                produto ->
                                        produto.getId()
                                                .equals(id)
                        )
                        .findFirst();

        return produtoEncontrado
                .map(ResponseEntity::ok)
                .orElseGet(
                        () ->
                                ResponseEntity
                                        .notFound()
                                        .build()
                );
    }

    // POST /produtos
    @PostMapping
    public ResponseEntity<Produto> adicionar(
            @RequestBody Produto novoProduto
    ) {

        novoProduto.setId(proximoId++);

        produtos.add(novoProduto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(novoProduto);
    }

    // PATCH /produtos/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<Produto> atualizar(
            @PathVariable Integer id,
            @RequestBody Produto dadosAtualizados
    ) {

        Optional<Produto> produtoEncontrado =
                produtos.stream()
                        .filter(
                                produto ->
                                        produto.getId()
                                                .equals(id)
                        )
                        .findFirst();

        if (produtoEncontrado.isEmpty()) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        Produto produto =
                produtoEncontrado.get();

        if (dadosAtualizados.getNome() != null) {

            produto.setNome(
                    dadosAtualizados.getNome()
            );
        }

        if (dadosAtualizados.getPreco() != null) {

            produto.setPreco(
                    dadosAtualizados.getPreco()
            );
        }

        return ResponseEntity.ok(produto);
    }

    // DELETE /produtos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Integer id
    ) {

        boolean removido =
                produtos.removeIf(
                        produto ->
                                produto.getId()
                                        .equals(id)
                );

        if (!removido) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity
                .noContent()
                .build();
    }
}