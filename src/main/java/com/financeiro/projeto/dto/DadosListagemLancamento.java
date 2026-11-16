package com.financeiro.projeto.dto;

import com.financeiro.projeto.entities.Lancamento;
import com.financeiro.projeto.entities.TipoLancamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosListagemLancamento(
        long id,
        String descricao,
        BigDecimal valor,
        TipoLancamento tipo,
        LocalDate dataVencimento
) {
    public DadosListagemLancamento(Lancamento lancamento){
        this(lancamento.getId(), lancamento.getDescricao(), lancamento.getValor(), lancamento.getTipo(), lancamento.getDataVencimento());
    }
}
