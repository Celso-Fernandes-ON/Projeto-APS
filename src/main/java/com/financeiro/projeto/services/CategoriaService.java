package com.financeiro.projeto.services;

import com.financeiro.projeto.dto.DadosCategoria;
import com.financeiro.projeto.dto.DadosDetalhamentoCategoria;
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
    private CategoriaRepository categoriaRepository;

    @Transactional
    public Categoria cadastrar(DadosCategoria dados) {
        var categoria = categoriaRepository.save(new Categoria(dados.nome()));
        return categoria;
    }

    public Page<DadosDetalhamentoCategoria> listar(Pageable paginacao){
        return categoriaRepository.findAll(paginacao).map(DadosDetalhamentoCategoria::new);
    }

}
