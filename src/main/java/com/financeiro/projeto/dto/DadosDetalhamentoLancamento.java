package com.financeiro.projeto.dto;

import com.financeiro.projeto.entities.Lancamento;
import com.financeiro.projeto.entities.StatusLancamento;
import com.financeiro.projeto.entities.TipoLancamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosDetalhamentoLancamento(
        Long id,
        String descricao,
        StatusLancamento status,
        BigDecimal valor,
        TipoLancamento tipo,
        LocalDate dataVencimento,
        LocalDate dataPagamento,
        String observacao,
        Long idPessoa,
        String nomePessoa,
        Long idCategoria,
        String nomeCategoria

) {
    public DadosDetalhamentoLancamento(Lancamento lancamento){
        this(
                lancamento.getId(),
                lancamento.getDescricao(),
                lancamento.getStatus(),
                lancamento.getValor(),
                lancamento.getTipo(),
                lancamento.getDataVencimento(),
                lancamento.getDataPagamento(),
                lancamento.getObservacao(),
                lancamento.getPessoa().getId(),
                lancamento.getPessoa().getNome(),
                lancamento.getCategoria().getId(),
                lancamento.getCategoria().getNome());
    }
}
