package com.financeiro.projeto.entities;

import com.financeiro.projeto.dto.DadosCategoria;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "categoria")
@Entity(name = "Categoria")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Boolean ativo;

    public Categoria(String nome, Boolean ativo) {
        this.ativo = true;
        this.nome = nome;
    }
    public void atualizar(DadosCategoria dados) {
        this.nome = dados.nome();
    }
    public void desativar(){
        this.ativo = false;
    }
    public void reativar(){
        this.ativo = true;
    }
}

