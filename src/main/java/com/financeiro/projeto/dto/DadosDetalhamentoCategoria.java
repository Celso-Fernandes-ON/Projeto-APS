package com.financeiro.projeto.dto;

import com.financeiro.projeto.entities.Categoria;

public record DadosDetalhamentoCategoria(
        Long id,
        String nome,
        Boolean ativo
) {
    public DadosDetalhamentoCategoria(Categoria categoria){
        this(categoria.getId(), categoria.getNome(), categoria.getAtivo());
    }
}
