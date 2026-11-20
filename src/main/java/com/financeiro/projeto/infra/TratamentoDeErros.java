package com.financeiro.projeto.infra;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import tools.jackson.core.exc.StreamReadException;
import tools.jackson.databind.exc.InvalidFormatException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class TratamentoDeErros {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> Trata404 (EntityNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> Trata400 (IllegalArgumentException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosErroValidacao>> TrataValidacao (MethodArgumentNotValidException e){
        var erros = e.getFieldErrors().stream().map(DadosErroValidacao::new).toList();
        return ResponseEntity.badRequest().body(erros);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<DadosErroRequisicao> TrataLeituraJson(HttpMessageNotReadableException e){
        InvalidFormatException formato = null;
        boolean jsonQuebrado = false;
        for (Throwable t = e; t != null; t = t.getCause()){
            if(t instanceof InvalidFormatException f){
                formato = f; break;
            }
            if (t instanceof StreamReadException){
                jsonQuebrado = true;
            }
        }
        if (formato != null){
            Class<?> alvo = formato.getTargetType();
            if (alvo != null && alvo.isEnum()){
                return erro400("TIPO_INVALIDO", "Valor '" + formato.getValue() + "' não é aceito. Use um destes: " + Arrays.toString(alvo.getEnumConstants()));
            }
            if (alvo != null && LocalDate.class.isAssignableFrom(alvo)){
                return erro400("DATA_INVALIDA", "Data '" + formato.getValue() + "' fora do formato. Use AAAA-MM-DD, por exemplo 2026-12-31.");
            }
            return erro400("VALOR_INVALIDO", "Valor '" + formato.getValue() + "' inválido para o tipo esperado.");
        }
        return erro400("JSON_INVALIDO", jsonQuebrado ? "JSON malformado. Verifique vírgulas, chaves e aspas." : "Corpo da requisição ausente ou ilegível." );
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<DadosErroRequisicao> TrataParametro(MethodArgumentTypeMismatchException e){
        return erro400("PARAMETRO_INVALIDO","Valor '" + e.getValue() + "' inválido para '" + e.getName() + "'.");
    }
    private ResponseEntity<DadosErroRequisicao> erro400(String codigo, String mensagem){
        return ResponseEntity.badRequest().body(new DadosErroRequisicao(codigo, mensagem));
    }
    public record DadosErroValidacao(String campo,String mensagem){
        public DadosErroValidacao(FieldError erro){
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
    public record DadosErroRequisicao(String codigo, String mensagem){}

}
