package com.financeiro.projeto.dto;

import com.financeiro.projeto.entities.Endereco;
import com.financeiro.projeto.entities.Pessoa;

public record DadosDetalhamentoPessoa(

        Long id,
        String nome,
        Boolean ativo,
        Endereco endereco
        ) {

    public DadosDetalhamentoPessoa(Pessoa pessoa) {
        this(pessoa.getId(),pessoa.getNome(),pessoa.getAtivo(), pessoa.getEndereco());
    }
}
