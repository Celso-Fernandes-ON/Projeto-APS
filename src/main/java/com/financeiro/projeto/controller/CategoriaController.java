package com.financeiro.projeto.controller;


import com.financeiro.projeto.dto.DadosCategoria;
import com.financeiro.projeto.dto.DadosDetalhamentoCategoria;
import com.financeiro.projeto.services.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("categoria")
public class CategoriaController {
    @Autowired private CategoriaService service;

    @PostMapping
    public ResponseEntity<DadosDetalhamentoCategoria> cadastra(@RequestBody @Valid DadosCategoria dados){
        var categoria = service.cadastrar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DadosDetalhamentoCategoria(categoria));
    }
    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoCategoria>> listar(
            @PageableDefault(size = 10, sort = "nome")Pageable papaginacao){
        return ResponseEntity.ok(service.listar(papaginacao));
    }

}
