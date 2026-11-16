package com.financeiro.projeto.services;

import com.financeiro.projeto.dto.DadosAtualizarLancamento;
import com.financeiro.projeto.dto.DadosCadastroLancamento;
import com.financeiro.projeto.dto.DadosListagemLancamento;
import com.financeiro.projeto.entities.Categoria;
import com.financeiro.projeto.entities.Lancamento;
import com.financeiro.projeto.repositorio.CategoriaRepository;
import com.financeiro.projeto.repositorio.LancamentoRepository;
import com.financeiro.projeto.repositorio.PessoaRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private CategoriaRepository categoriaRepository;

    @Transactional
    public Lancamento cadastrar(DadosCadastroLancamento dados){
        var pessoa = pessoaRepository.findById(dados.idPessoa()).orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));
        if (!pessoa.getAtivo()) throw new IllegalArgumentException("Pessoa inativa");
        var categoria = categoriaRepository.findById(dados.idCategoria()).orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
        return repository.save(new Lancamento(dados, pessoa, categoria));
    }
    public Page<DadosListagemLancamento> listar(Pageable paginacao){
        return repository.findAll(paginacao).map(DadosListagemLancamento::new);
    }
    public Lancamento detalhar(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lançamento não encontrado"));
    }
    @Transactional
    public Lancamento atualizar(DadosAtualizarLancamento dados){
        var lancamento = detalhar(dados.id());
        lancamento.atualizarInformacoes(dados);
        return lancamento;
    }
    @Transactional
    public void excluir(Long id){
        var lancamento = detalhar(id);
        repository.delete(lancamento);
    }

}
