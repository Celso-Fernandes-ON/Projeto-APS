package com.financeiro.projeto.dto;

import com.financeiro.projeto.entities.Pessoa;

public record DadosDetalhamentoPessoa(

        Long id,
        String nome,
        Boolean ativo,
        DadosEndereco endereco
        ) {

    public DadosDetalhamentoPessoa(Pessoa pessoa) {
        this(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getAtivo(),
                new DadosEndereco(
                    pessoa.getEndereco().getLogradouro(),
                    pessoa.getEndereco().getNumero(),
                    pessoa.getEndereco().getComplemento(),
                    pessoa.getEndereco().getBairro(),
                    pessoa.getEndereco().getCep(),
                    pessoa.getEndereco().getCidade(),
                    pessoa.getEndereco().getUf()
                )
        );
    }
}
