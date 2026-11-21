package com.financeiro.projeto.controller;


import com.financeiro.projeto.dto.DadosCategoria;
import com.financeiro.projeto.dto.DadosDetalhamentoCategoria;
import com.financeiro.projeto.services.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("categoria")
public class CategoriaController {
    @Autowired
    private CategoriaService service;

    @PostMapping
    public ResponseEntity<DadosDetalhamentoCategoria> cadastra(@RequestBody @Valid DadosCategoria dados, UriComponentsBuilder uriBuilder){
        var categoria = service.cadastrar(dados);
        var uri = uriBuilder.path("/categoria/{id}").buildAndExpand(categoria.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoCategoria(categoria));
    }
    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoCategoria>> listar(
            @RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 10, sort = "nome")Pageable paginacao){
        return ResponseEntity.ok(service.listar(ativo, paginacao));
    }
    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoCategoria> detalhar(@PathVariable Long id){
        var categoria = service.detalharCategoria(id);
        return ResponseEntity.ok(new DadosDetalhamentoCategoria(categoria));
    }
    @PutMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoCategoria> atualizar(@PathVariable Long id, @RequestBody @Valid DadosCategoria dados){
        var categoria = service.atualizar(id, dados);
        return ResponseEntity.ok(new DadosDetalhamentoCategoria(categoria));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id){
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> reativar(@PathVariable Long id){
        service.reativar(id);
        return ResponseEntity.noContent().build();
    }
}
