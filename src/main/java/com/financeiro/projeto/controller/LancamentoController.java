package com.financeiro.projeto.controller;

import com.financeiro.projeto.dto.DadosDetalhamentoLancamento;
import com.financeiro.projeto.services.LancamentoService;
import jakarta.validation.Valid;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("lancamento")
public class LancamentoController {
    @Autowired
    private LancamentoService service;

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoLancamento>> listar(@PageableDefault(size = 10, sort = "dataVencimento")Pageable paginacao){
        return ResponseEntity.ok(service.listar(paginacao));
    }
    @PostMapping
    public ResponseEntity<DadosDetalhamentoLancamento> cadastrar(@RequestBody @Valid DadosDetalhamentoLancamento dados){
        var lancamento = service.cadastrar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DadosDetalhamentoLancamento(lancamento));
    }
}
