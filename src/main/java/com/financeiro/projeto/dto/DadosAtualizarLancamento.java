package com.financeiro.projeto.dto;

import com.financeiro.projeto.entities.TipoLancamento;
import jakarta.validation.constraints.NotBlank;
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
    String observacao,

) {
}
