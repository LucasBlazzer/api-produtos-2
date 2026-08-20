package com.lucas.produtoscategorias.service;

import com.lucas.produtoscategorias.dto.CategoriaRequest;
import com.lucas.produtoscategorias.dto.CategoriaResponse;
import com.lucas.produtoscategorias.entity.Categoria;
import com.lucas.produtoscategorias.exception.RecursoNaoEncontradoException;
import com.lucas.produtoscategorias.exception.RegraNegocioException;
import com.lucas.produtoscategorias.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public CategoriaResponse salvar(CategoriaRequest request) {
        validarNomeDuplicado(request.nome());

        Categoria categoria = new Categoria(
                request.nome().trim(),
                request.descricao()
        );

        return toResponse(categoriaRepository.save(categoria));
    }

    @Transactional
    public List<CategoriaResponse> salvarTodos(List<CategoriaRequest> requests) {
        List<Categoria> categorias = requests.stream()
                .map(request -> {
                    validarNomeDuplicado(request.nome());
                    return new Categoria(request.nome().trim(), request.descricao());
                })
                .toList();

        return categoriaRepository.saveAll(categorias)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Categoria buscarEntidadePorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Categoria não encontrada. ID: " + id
                ));
    }

    @Transactional(readOnly = true)
    public CategoriaResponse buscarPorId(Long id) {
        return toResponse(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listarTodas() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void deletar(Long id) {
        Categoria categoria = buscarEntidadePorId(id);
        categoriaRepository.delete(categoria);
    }

    private void validarNomeDuplicado(String nome) {
        categoriaRepository.findByNomeIgnoreCase(nome.trim())
                .ifPresent(categoria -> {
                    throw new RegraNegocioException(
                            "Já existe uma categoria com o nome: " + nome
                    );
                });
    }

    private CategoriaResponse toResponse(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao()
        );
    }
}
