package com.lucas.produtoscategorias.controller;

import com.lucas.produtoscategorias.dto.ProdutoRequest;
import com.lucas.produtoscategorias.dto.ProdutoResponse;
import com.lucas.produtoscategorias.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> cadastrar(
            @Valid @RequestBody ProdutoRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(produtoService.salvar(request));
    }

    @PostMapping("/lote")
    public ResponseEntity<List<ProdutoResponse>> cadastrarLote(
            @RequestBody List<@Valid ProdutoRequest> requests
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(produtoService.salvarTodos(requests));
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscar(@PathVariable Long id) {
        return produtoService.buscarPorId(id);
    }

    @GetMapping
    public Page<ProdutoResponse> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "asc") String direcao
    ) {
        return produtoService.listarPaginado(page, size, direcao);
    }

    @GetMapping("/buscar")
    public Page<ProdutoResponse> filtrar(
            @RequestParam String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "asc") String direcao
    ) {
        return produtoService.filtrarPorNome(nome, page, size, direcao);
    }

    @GetMapping("/nome-exato")
    public Page<ProdutoResponse> buscarNomeExato(
            @RequestParam String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "asc") String direcao
    ) {
        return produtoService.buscarNomeExatoComJpql(nome, page, size, direcao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
