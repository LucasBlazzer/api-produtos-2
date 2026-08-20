package com.lucas.produtoscategorias.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(
        @NotBlank(message = "O nome da categoria é obrigatório")
        @Size(max = 100)
        String nome,

        @Size(max = 500)
        String descricao
) {
}
