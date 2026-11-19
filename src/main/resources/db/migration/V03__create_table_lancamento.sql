CREATE TABLE lancamento (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL,
    status varchar(20)NOT NULL,
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    valor NUMERIC(10,2) NOT NULL,
    observacao VARCHAR(200),
    tipo VARCHAR(20) NOT NULL,
    id_pessoa BIGINT NOT NULL REFERENCES pessoa(id),
    id_categoria BIGINT NOT NULL REFERENCES categoria(id)
);