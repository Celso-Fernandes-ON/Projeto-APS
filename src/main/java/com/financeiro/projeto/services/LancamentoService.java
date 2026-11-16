package com.financeiro.projeto.services;

import com.financeiro.projeto.dto.DadosCadastroLancamento;
import com.financeiro.projeto.dto.DadosListagemLancamento;
import com.financeiro.projeto.dto.DadosListagemPessoa;
import com.financeiro.projeto.entities.Lancamento;
import com.financeiro.projeto.repositorio.CategoriaRepository;
import com.financeiro.projeto.repositorio.LancamentoRepository;
import com.financeiro.projeto.repositorio.PessoaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        var pessoa = pessoaRepository.findById(dados.idPessoa());
        var categoria = CategoriaRepository.findById(dados.idCategoria());
        return repository.save(new Lancamento(dados, pessoa, categoria));
    }
    public Page<DadosListagemLancamento> listar(Pageable paginacao){
        return repository.findAll(paginacao).map(DadosListagemLancamento::new);
    }
}
