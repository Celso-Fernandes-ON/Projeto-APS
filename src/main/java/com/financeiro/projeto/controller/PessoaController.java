package com.financeiro.projeto.controller;

import com.financeiro.projeto.dto.DadosAtualizarPessoas;
import com.financeiro.projeto.dto.DadosCadastroPessoa;
import com.financeiro.projeto.dto.DadosDetalhamentoPessoa;
import com.financeiro.projeto.dto.DadosListagemPessoa;
import com.financeiro.projeto.services.PessoaServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("pessoas")
public class PessoaController {

    @Autowired
    private PessoaServices services;

    @PostMapping
    public ResponseEntity<DadosDetalhamentoPessoa> cadastrar(@RequestBody @Valid
        DadosCadastroPessoa dados,
        UriComponentsBuilder uriBuilder){
        var pessoa = services.cadastrar(dados);
        var uri = uriBuilder.path("/pessoas/{id}").buildAndExpand(pessoa.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoPessoa(pessoa));
    }
    @GetMapping
    public ResponseEntity<Page<DadosListagemPessoa>> Listar(@PageableDefault(
            size = 10, sort = {"nome"}) Pageable peginacao){
        Page<DadosListagemPessoa> page =
        services.listarPessoa(peginacao);
        return ResponseEntity.ok(page);
    }
    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoPessoa> detalhar(@PathVariable Long id){
        var pessoa = services.detalharPessoa(id);
        return ResponseEntity.ok(new DadosDetalhamentoPessoa(pessoa));
    }
    @PutMapping
    public ResponseEntity<DadosDetalhamentoPessoa> atualizar(@RequestBody @Valid DadosAtualizarPessoas dados){
        var pessoa = services.atualizarPessoa(dados);
        return ResponseEntity.ok(new DadosDetalhamentoPessoa(pessoa));
    }
    @PatchMapping("/reativar/{id}")
    public ResponseEntity<Void> reativar(@PathVariable Long id){
        services.reativarPessoa(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        services.excluir(id);
        return ResponseEntity.noContent().build();
    }
}