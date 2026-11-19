package com.financeiro.projeto.entities;

import com.financeiro.projeto.dto.DadosAtualizarLancamento;
import com.financeiro.projeto.dto.DadosCadastroLancamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Table(name = "lancamento")
@Entity(name = "Lancamento")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Lancamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;

    @Enumerated(EnumType.STRING)
    private StatusLancamento status;

    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private BigDecimal valor;
    private String observacao;

    @Enumerated(EnumType.STRING)
    private TipoLancamento tipo;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_pessoa")
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    public Lancamento(DadosCadastroLancamento dados, Pessoa pessoa, Categoria categoria) {
        this.descricao = dados.descricao();
        this.status = StatusLancamento.REALIZADO;
        this.dataVencimento = dados.dataVencimento();
        this.dataPagamento = dados.dataPagamento();
        this.valor = dados.valor();
        this.observacao = dados.observacao();
        this.tipo = dados.tipo();
        this.pessoa = pessoa;
        this.categoria = categoria;
    }
    public void atualizarInformacoes(DadosAtualizarLancamento dados){
        if (dados.descricao() != null) this.descricao = dados.descricao();
        if (dados.dataVencimento() != null) this.dataVencimento = dados.dataVencimento();
        if (dados.dataPagamento() != null) this.dataPagamento = dados.dataPagamento();
        if (dados.valor() != null) this.valor = dados.valor();
        if (dados.observacao() != null) this.observacao = dados.observacao();
    }
    public void cancelar() {
        if (this.status == StatusLancamento.CANCELADO) {
            throw new IllegalArgumentException("Lançamento já está cancelado");
        }
        this.status = StatusLancamento.CANCELADO;
    }

    public boolean estaCancelado() {
        return this.status == StatusLancamento.CANCELADO;
    }
}
