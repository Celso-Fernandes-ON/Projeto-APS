package com.financeiro.projeto.dto;

import com.financeiro.projeto.entities.Pessoa;

public record DadosDetalhamentoPessoa(

        Long id,
        String nome,
        Boolean ativo
        ) {

    public DadosDetalhamentoPessoa(Pessoa pessoa) {
        this(pessoa.getId(),pessoa.getNome(),pessoa.getAtivo());
    }
}
