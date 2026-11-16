package com.financeiro.projeto.infra;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class TratamentoDeErros {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> Trata404 (EntityNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> Trata404 (IllegalArgumentException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosErroValidacao>> TrataValidacao (MethodArgumentNotValidException e){
        var erros = e.getFieldErrors().stream().map(DadosErroValidacao::new).toList();
        return ResponseEntity.badRequest().body(erros);
    }
    public record DadosErroValidacao(String campo,String mensagem){
        public DadosErroValidacao(FieldError erro){
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}
