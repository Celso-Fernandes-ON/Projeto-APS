package com.financeiro.projeto.services;

import com.financeiro.projeto.dto.DadosCadastroPessoa;
import com.financeiro.projeto.dto.DadosDetalhamentoCategoria;
import com.financeiro.projeto.dto.DadosDetalhamentoPessoa;
import com.financeiro.projeto.dto.DadosListagemPessoa;
import com.financeiro.projeto.entities.Categoria;
import com.financeiro.projeto.repositorio.CategoriaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository repository;

    @Transactional
    public Categoria cadastrar(DadosCategoria dados) {
        var categoria = repository.save(new Categoria(dados));
        return categoria;
    }

    public Page<DadosDetalhamentoCategoria> listar(Pageable paginacao){
        return repository.findAll(paginacao).map(DadosDetalhamentoCategoria::new);
    }

}
