package com.financeiro.projeto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("lancamento")
public class LancamentoController {
    @GetMapping
    public void listar(){

    }
    @PostMapping
    public void cadastrar(){

    }
}
