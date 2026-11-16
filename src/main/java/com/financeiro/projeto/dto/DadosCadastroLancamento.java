package com.financeiro.projeto.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroLancamento {
    @NotBlank
    String nome;
    @NotNull @Valid DadosCategoria categoria;
}
