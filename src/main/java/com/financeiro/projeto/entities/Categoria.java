package com.financeiro.projeto.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Table(name = "categoria")
@Entity(name = "Categoria")
@Getter
public class Categoria {
    private Long id;
    private String name;

    public Categoria(String name) {
        this.name = name;
    }

}

