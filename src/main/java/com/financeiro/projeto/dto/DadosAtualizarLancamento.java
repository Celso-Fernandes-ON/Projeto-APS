package com.financeiro.projeto.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosAtualizarLancamento(
    @NotNull
    Long id,
    String descricao,
    LocalDate dataVencimento,
    LocalDate dataPagamento,
    @Positive
    BigDecimal valor,
    String observacao

) {
}
