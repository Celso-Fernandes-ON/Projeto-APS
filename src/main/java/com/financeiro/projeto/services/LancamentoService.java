package com.financeiro.projeto.services;

import com.financeiro.projeto.dto.DadosCadastroLancamento;
import com.financeiro.projeto.entities.Lancamento;
import com.financeiro.projeto.repositorio.LancamentoRepository;
import com.financeiro.projeto.repositorio.PessoaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LancamentoService {
    @Autowired
    private LancamentoRepository repository;
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private CategoriaService categoriaService;

    @Transactional
    public Lancamento cadastrar(DadosCadastroLancamento dados){
        pessoaRepository.findById(dados.idPessoa());
    }
}
