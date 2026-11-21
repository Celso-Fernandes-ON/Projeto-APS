# Projeto APS — Sistema Financeiro

API REST para registrar **débitos e créditos** (receitas e despesas). O sistema tem CRUD de **Pessoas** (clientes, fornecedores etc.), **Categorias** e **Lançamentos**.

Regra central: **nenhum dado financeiro é apagado**. Pessoas e categorias ficam inativas, e lançamentos ficam cancelados. Os registros continuam no banco para consulta e auditoria.

## Tecnologias

- Java 21
- Spring Boot 4.1.1 (Spring Web, Spring Data JPA, Bean Validation)
- Lombok
- PostgreSQL
- Flyway (versionamento do banco)
- Maven (wrapper incluído)

## Pré-requisitos

1. **Java 21** instalado (`java -version`).
2. **PostgreSQL** ligado na porta `5432`.
3. Acesso à internet na primeira execução, para o Maven baixar as dependências.

## Como rodar

**1. Crie o banco.** O Flyway cria as tabelas, mas não cria o banco:

```sql
CREATE DATABASE financeiro;
```

**2. Defina a senha do PostgreSQL** na variável de ambiente `DB_PASSWORD`. O Spring não lê o arquivo `.env` sozinho.

Linux ou macOS:
```bash
export DB_PASSWORD=sua_senha
```

Windows (PowerShell):
```powershell
$env:DB_PASSWORD="sua_senha"
```

Na IDE (IntelliJ): *Run → Edit Configurations → Environment variables* → `DB_PASSWORD=sua_senha`.

O usuário usado é `postgres` e o banco é `financeiro`. Para mudar, edite `src/main/resources/application.properties`.

**3. Suba a aplicação:**

```bash
./mvnw spring-boot:run
```
No Windows: `mvnw.cmd spring-boot:run`.

No log, confira a mensagem do Flyway: `Successfully applied 3 migrations`. A API fica em `http://localhost:8080`.

> Para recomeçar do zero, apague e recrie o banco (`DROP DATABASE financeiro; CREATE DATABASE financeiro;`) e suba a aplicação de novo.

## Regras de negócio

### Pessoa
- Nasce **ativa**. `DELETE` apenas marca como **inativa** (`ativo = false`).
- A listagem mostra só as ativas por padrão. Para ver as inativas: `GET /pessoas?ativo=false`. A busca por id mostra também as inativas, com o campo `ativo`.
- Pessoa inativa **não pode ser alterada** e **não pode receber novos lançamentos**.
- `PATCH /pessoas/{id}` reativa.
- Inativar uma pessoa já inativa, ou reativar uma já ativa, retorna 400 (`Pessoa já está inativa` / `Pessoa já está ativa`).
- O CEP deve ter 8 dígitos.

### Categoria
- Mesma lógica de Pessoa: `DELETE` desativa, a listagem mostra só as ativas (`GET /categoria?ativo=false` lista as inativas), e categoria inativa não pode ser alterada nem usada em novos lançamentos.
- `PATCH /categoria/{id}` reativa.
- Desativar uma categoria já inativa, ou reativar uma já ativa, retorna 400 (`Categoria já está inativa` / `Categoria já está ativa`).

### Lançamento
- `tipo`: `RECEITA` ou `DESPESA`. O `valor` deve ser positivo.
- `status`:

| Status | Quando |
|---|---|
| `PENDENTE` | Criado **sem** `dataPagamento` |
| `REALIZADO` | Criado **com** `dataPagamento`, ou quando a data de pagamento é informada depois (PUT) |
| `CANCELADO` | Resultado do `DELETE` |

- A `dataPagamento` não pode ser anterior à `dataVencimento`. A regra vale no cadastro e na atualização (no PUT, considera o valor que ficará após a alteração) e retorna 400.
- `DELETE` **cancela** o lançamento, sem apagar. Um lançamento cancelado não pode ser alterado nem cancelado de novo.
- A listagem padrão mostra pendentes e realizados. Para ver por status: `GET /lancamento?status=CANCELADO`.

## Endpoints

### Pessoas — `/pessoas`

| Método | Rota | Descrição | Resposta |
|---|---|---|---|
| POST | `/pessoas` | Cadastrar | 201 + `Location` |
| GET | `/pessoas` | Listar ativas (paginado) | 200 |
| GET | `/pessoas?ativo=false` | Listar inativas (paginado) | 200 |
| GET | `/pessoas/{id}` | Detalhar | 200 |
| PUT | `/pessoas` | Atualizar (`id` no corpo) | 200 |
| DELETE | `/pessoas/{id}` | Inativar | 204 |
| PATCH | `/pessoas/{id}` | Reativar | 204 |

### Categorias — `/categoria`

| Método | Rota | Descrição | Resposta |
|---|---|---|---|
| POST | `/categoria` | Cadastrar | 201 + `Location` |
| GET | `/categoria` | Listar ativas (paginado) | 200 |
| GET | `/categoria?ativo=false` | Listar inativas (paginado) | 200 |
| GET | `/categoria/{id}` | Detalhar | 200 |
| PUT | `/categoria/{id}` | Atualizar nome (`id` na URL) | 200 |
| DELETE | `/categoria/{id}` | Desativar | 204 |
| PATCH | `/categoria/{id}` | Reativar | 204 |

### Lançamentos — `/lancamento`

| Método | Rota | Descrição | Resposta |
|---|---|---|---|
| POST | `/lancamento` | Cadastrar | 201 + `Location` |
| GET | `/lancamento` | Listar não cancelados (paginado) | 200 |
| GET | `/lancamento?status=PENDENTE` | Listar por status | 200 |
| GET | `/lancamento/{id}` | Detalhar | 200 |
| PUT | `/lancamento` | Atualizar (`id` no corpo) | 200 |
| DELETE | `/lancamento/{id}` | Cancelar | 204 |

As listagens aceitam paginação: `?page=0&size=10&sort=nome`. O padrão é 10 por página, ordenado por `nome` (pessoas e categorias) ou `dataVencimento` (lançamentos).

## Exemplos de requisição

**Cadastrar pessoa** — `POST /pessoas`
```json
{
  "nome": "Maria Silva",
  "endereco": {
    "logradouro": "Rua das Flores",
    "numero": "100",
    "complemento": "Casa",
    "bairro": "Centro",
    "cep": "59000000",
    "cidade": "Natal",
    "uf": "RN"
  }
}
```

**Atualizar pessoa** — `PUT /pessoas` (só os campos enviados mudam)
```json
{ "id": 1, "nome": "Maria Souza", "endereco": { "cidade": "Parnamirim" } }
```

**Cadastrar categoria** — `POST /categoria`
```json
{ "nome": "Alimentação" }
```

**Cadastrar lançamento** — `POST /lancamento` (sem `dataPagamento` fica `PENDENTE`)
```json
{
  "descricao": "Mercado",
  "dataVencimento": "2026-12-01",
  "valor": 150.00,
  "observacao": "Compra do mês",
  "tipo": "DESPESA",
  "idPessoa": 1,
  "idCategoria": 1
}
```

**Informar o pagamento** — `PUT /lancamento` (o status passa a `REALIZADO`)
```json
{ "id": 1, "dataPagamento": "2026-12-02" }
```

## Tratamento de erros

| Situação | Status | Resposta |
|---|---|---|
| Registro não encontrado | 404 | Texto, por exemplo `Pessoa não encontrada` |
| Regra de negócio violada (pessoa inativa, lançamento cancelado, pagamento antes do vencimento etc.) | 400 | Texto com o motivo |
| Campo obrigatório ausente ou inválido | 400 | Lista de `{ campo, mensagem }` |
| Valor de `tipo` ou `status` inválido | 400 | `{ "codigo": "TIPO_INVALIDO", "mensagem": "... Use um destes: [RECEITA, DESPESA]" }` |
| Data fora do formato `AAAA-MM-DD` | 400 | `{ "codigo": "DATA_INVALIDA", ... }` |
| JSON malformado ou corpo ausente | 400 | `{ "codigo": "JSON_INVALIDO", ... }` |
| Outro valor com tipo errado (por exemplo, texto em `valor`) | 400 | `{ "codigo": "VALOR_INVALIDO", ... }` |
| Parâmetro inválido na URL (por exemplo, `?status=XYZ`) | 400 | `{ "codigo": "PARAMETRO_INVALIDO", ... }` |

## Modelo de dados

```
Pessoa (id, nome, endereço, ativo)
Categoria (id, nome, ativo)
Lancamento (id, descricao, status, dataVencimento, dataPagamento,
            valor, observacao, tipo, pessoa, categoria)
```

Cada lançamento pertence a uma pessoa e a uma categoria. As tabelas são criadas pelas migrations em `src/main/resources/db/migration` (`V01` a `V03`).

## Estrutura do projeto

```
src/main/java/com/financeiro/projeto/
├── controller/   endpoints REST
├── services/     regras de negócio
├── repositorio/  acesso ao banco (Spring Data JPA)
├── entities/     entidades e enums
├── dto/          objetos de entrada e saída da API
└── infra/        tratamento de erros
```

## Testando a API

O arquivo `docs/Projeto-APS.yaml` é uma coleção do **Insomnia** com as requisições na ordem de uso. Importe em *Import*. O endereço base fica na variável `base_url` do ambiente (*Manage Environments*), e cada requisição indica o resultado esperado. Os ids usados nas requisições (`1`, `2`) devem ser ajustados conforme os registros criados.
