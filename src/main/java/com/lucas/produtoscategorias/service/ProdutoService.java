package com.lucas.produtoscategorias.service;

import com.lucas.produtoscategorias.dto.CategoriaResponse;
import com.lucas.produtoscategorias.dto.ProdutoRequest;
import com.lucas.produtoscategorias.dto.ProdutoResponse;
import com.lucas.produtoscategorias.entity.Categoria;
import com.lucas.produtoscategorias.entity.Produto;
import com.lucas.produtoscategorias.exception.RecursoNaoEncontradoException;
import com.lucas.produtoscategorias.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaService categoriaService;

    public ProdutoService(
            ProdutoRepository produtoRepository,
            CategoriaService categoriaService
    ) {
        this.produtoRepository = produtoRepository;
        this.categoriaService = categoriaService;
    }

    @Transactional
    public ProdutoResponse salvar(ProdutoRequest request) {
        Categoria categoria = categoriaService.buscarEntidadePorId(request.categoriaId());

        Produto produto = new Produto(
                request.nome().trim(),
                request.descricao(),
                request.preco(),
                categoria
        );

        return toResponse(produtoRepository.save(produto));
    }

    @Transactional
    public List<ProdutoResponse> salvarTodos(List<ProdutoRequest> requests) {
        List<Produto> produtos = requests.stream()
                .map(request -> new Produto(
                        request.nome().trim(),
                        request.descricao(),
                        request.preco(),
                        categoriaService.buscarEntidadePorId(request.categoriaId())
                ))
                .toList();

        return produtoRepository.saveAll(produtos)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Produto não encontrado. ID: " + id
                ));

        return toResponse(produto);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResponse> listarPaginado(
            int page,
            int size,
            String direcao
    ) {
        Sort sort = criarOrdenacao(direcao);
        Pageable pageable = PageRequest.of(page, size, sort);

        return produtoRepository.findAll(pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResponse> filtrarPorNome(
            String nome,
            int page,
            int size,
            String direcao
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                criarOrdenacao(direcao)
        );

        return produtoRepository
                .findByNomeContainingIgnoreCase(nome, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResponse> buscarNomeExatoComJpql(
            String nome,
            int page,
            int size,
            String direcao
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                criarOrdenacao(direcao)
        );

        return produtoRepository
                .buscarPorNomeExato(nome, pageable)
                .map(this::toResponse);
    }

    @Transactional
    public void deletar(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Produto não encontrado. ID: " + id
                ));

        produtoRepository.delete(produto);
    }

    private Sort criarOrdenacao(String direcao) {
        Sort.Direction direction = "desc".equalsIgnoreCase(direcao)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return Sort.by(direction, "nome");
    }

    private ProdutoResponse toResponse(Produto produto) {
        Categoria categoria = produto.getCategoria();

        CategoriaResponse categoriaResponse = new CategoriaResponse(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao()
        );

        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                categoriaResponse
        );
    }
}
