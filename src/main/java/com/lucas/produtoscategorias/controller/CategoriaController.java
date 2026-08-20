package com.lucas.produtoscategorias.controller;

import com.lucas.produtoscategorias.dto.CategoriaRequest;
import com.lucas.produtoscategorias.dto.CategoriaResponse;
import com.lucas.produtoscategorias.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> cadastrar(
            @Valid @RequestBody CategoriaRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoriaService.salvar(request));
    }

    @PostMapping("/lote")
    public ResponseEntity<List<CategoriaResponse>> cadastrarLote(
            @RequestBody List<@Valid CategoriaRequest> requests
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoriaService.salvarTodos(requests));
    }

    @GetMapping
    public List<CategoriaResponse> listar() {
        return categoriaService.listarTodas();
    }

    @GetMapping("/{id}")
    public CategoriaResponse buscar(@PathVariable Long id) {
        return categoriaService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
