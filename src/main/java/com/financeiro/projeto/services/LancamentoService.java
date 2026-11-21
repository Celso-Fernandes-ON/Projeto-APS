package com.financeiro.projeto.services;

import com.financeiro.projeto.dto.DadosAtualizarLancamento;
import com.financeiro.projeto.dto.DadosCadastroLancamento;
import com.financeiro.projeto.dto.DadosListagemLancamento;
import com.financeiro.projeto.entities.Lancamento;
import com.financeiro.projeto.entities.StatusLancamento;
import com.financeiro.projeto.repositorio.CategoriaRepository;
import com.financeiro.projeto.repositorio.LancamentoRepository;
import com.financeiro.projeto.repositorio.PessoaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LancamentoService {
    @Autowired
    private LancamentoRepository repository;
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    private void validarDatas(LocalDate vencimento, LocalDate pagamento) {
        if (pagamento != null && pagamento.isBefore(vencimento)) {
            throw new IllegalArgumentException(
                    "Data de pagamento não pode ser anterior à data de vencimento");
        }
    }
    @Transactional
    public Lancamento cadastrar(DadosCadastroLancamento dados){
        var pessoa = pessoaRepository.findById(dados.idPessoa()).orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));
        if (!pessoa.getAtivo()) throw new IllegalArgumentException("Pessoa inativa");
        var categoria = categoriaRepository.findById(dados.idCategoria()).orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
        if (!categoria.getAtivo()) throw new IllegalArgumentException("Categoria inativa");
        validarDatas(dados.dataVencimento(), dados.dataPagamento());
        return repository.save(new Lancamento(dados, pessoa, categoria));
    }
    public Page<DadosListagemLancamento> listar(StatusLancamento status, Pageable paginacao){
        var page = (status == null)
                ? repository.findAllByStatusNot(StatusLancamento.CANCELADO, paginacao)
                : repository.findAllByStatus(status, paginacao);
        return page.map(DadosListagemLancamento::new);
    }
    public Lancamento detalhar(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lançamento não encontrado"));
    }
    @Transactional
    public Lancamento atualizar(DadosAtualizarLancamento dados){
        var lancamento = detalhar(dados.id());
        if(lancamento.estaCancelado()){
            throw new IllegalArgumentException("Lançamento cancelado não pode ser alterado");
        }
        var vencimento = dados.dataVencimento() != null ? dados.dataVencimento() : lancamento.getDataVencimento();
        var pagamento  = dados.dataPagamento()  != null ? dados.dataPagamento()  : lancamento.getDataPagamento();

        validarDatas(vencimento, pagamento);

        lancamento.atualizarInformacoes(dados);
        return lancamento;
    }
    @Transactional
    public void excluir(Long id){
        detalhar(id).cancelar();
    }

}
