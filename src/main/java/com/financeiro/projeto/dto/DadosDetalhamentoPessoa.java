package com.financeiro.projeto.dto;

import com.financeiro.projeto.entities.Endereco;
import com.financeiro.projeto.entities.Pessoa;

public record DadosDetalhamentoPessoa(

        Long id,
        String nome,
        Boolean ativo,
        String logradouro,
        String bairro,
        String cep,
        String numero,
        String complemento,
        String cidade,
        String uf
        ) {

    public DadosDetalhamentoPessoa(Pessoa pessoa, DadosEndereco endereco) {
        this(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getAtivo(),
                pessoa.getEndereco().getBairro(),
                pessoa.getEndereco().getUf(),
                pessoa.getEndereco().getCidade(),
                pessoa.getEndereco().getNumero(),
                pessoa.getEndereco().getCep(),
                pessoa.getEndereco().getLogradouro(),
                pessoa.getEndereco().getComplemento()
        );
    }
}
