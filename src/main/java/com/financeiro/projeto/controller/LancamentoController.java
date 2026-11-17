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
import org.springframework.web.util.UriComponentsBuilder;

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
    public ResponseEntity<DadosDetalhamentoLancamento> cadastrar(@RequestBody @Valid DadosCadastroLancamento dados, UriComponentsBuilder uriBuilder){
        var lancamento = service.cadastrar(dados);
        var uri = uriBuilder.path("/{id}").buildAndExpand(lancamento.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoLancamento(lancamento));
//        return ResponseEntity.status(HttpStatus.CREATED).body(new DadosDetalhamentoLancamento(lancamento));
    }
    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoLancamento> detalhar(@PathVariable Long id){
        var lancamento = service.detalhar(id);
        return ResponseEntity.ok(new DadosDetalhamentoLancamento(lancamento));
    }
    @PutMapping
    public ResponseEntity<DadosDetalhamentoLancamento> atualizar(@RequestBody @Valid DadosAtualizarLancamento dados){
        return ResponseEntity.ok(new DadosDetalhamentoLancamento(service.atualizar(dados)));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
