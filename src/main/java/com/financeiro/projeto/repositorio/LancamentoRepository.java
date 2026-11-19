package com.financeiro.projeto.repositorio;

import com.financeiro.projeto.entities.Lancamento;
import com.financeiro.projeto.entities.StatusLancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
    Page<Lancamento> findAllByStatus(StatusLancamento status, Pageable paginacao);
}
