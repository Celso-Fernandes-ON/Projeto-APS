package com.financeiro.projeto.services;

import com.financeiro.projeto.dto.DadosCategoria;
import com.financeiro.projeto.dto.DadosDetalhamentoCategoria;
import com.financeiro.projeto.entities.Categoria;
import com.financeiro.projeto.repositorio.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
        return categoriaRepository.findAllByAtivoTrue(paginacao).map(DadosDetalhamentoCategoria::new);
    }
    public Categoria detalharCategoria(Long id){
        return categoriaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
    }
    @Transactional
    public Categoria atualizar(Long id, DadosCategoria dados){
        var categoria = detalharCategoria(id);
        if (!categoria.getAtivo()) throw new IllegalArgumentException("Categoria inativa não pode ser alterada");
        categoria.atualizar(dados);
        return categoria;
    }
    @Transactional
    public void desativar(Long id){
        detalharCategoria(id).desativar();
    }
    @Transactional
    public void reativar(Long id){
        detalharCategoria(id).reativar();
    }

}
