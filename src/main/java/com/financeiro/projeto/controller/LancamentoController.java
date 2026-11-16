package com.financeiro.projeto.controller;

import com.financeiro.projeto.dto.*;
import com.financeiro.projeto.services.LancamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<Page<DadosListagemLancamento>> listar(@PageableDefault(size = 10, sort = "dataVencimento")Pageable paginacao){
        return ResponseEntity.ok(service.listar(paginacao));
    }
    @PostMapping
    public ResponseEntity<DadosDetalhamentoLancamento> cadastrar(@RequestBody @Valid DadosCadastroLancamento dados){
        var lancamento = service.cadastrar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DadosDetalhamentoLancamento(lancamento));
    }
    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        var categoria = service.listar(id);
        return ResponseEntity.ok(new DadosDetalhamentoCategoria(categoria));
    }

}
