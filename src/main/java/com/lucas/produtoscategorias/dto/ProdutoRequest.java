package com.lucas.produtoscategorias.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProdutoRequest(
        @NotBlank(message = "O nome do produto é obrigatório")
        @Size(max = 150)
        String nome,

        @Size(max = 1000)
        String descricao,

        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
        BigDecimal preco,

        @NotNull(message = "A categoria é obrigatória")
        Long categoriaId
) {
}
