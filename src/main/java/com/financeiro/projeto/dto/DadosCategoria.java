package com.financeiro.projeto.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosCategoria {
    @NotBlank
    String nome;
}
