# Virtual Library API

API REST para gerenciamento de uma biblioteca virtual. O projeto foi criado como estudo prático de Java e Spring Boot, com foco em autenticação, organização em camadas, persistência com PostgreSQL, migrations e integração com API externa de livros.

## Funcionalidades

- Cadastro e login de usuários.
- Autenticação com JWT.
- Controle de acesso por roles (`USER` e `ADMIN`).
- CRUD de livros no catálogo para usuários administradores.
- Listagem e busca de livros por título, autor, gênero e ISBN.
- Busca de livros na Google Books API.
- Importação de livros externos para o catálogo local.
- Biblioteca pessoal do usuário autenticado.
- Controle de status e formato de leitura.
- Tratamento padronizado de erros.
- Migrations de banco com Flyway.
- Testes unitários para services.

## Tecnologias

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL
- H2 Database para testes
- Flyway
- MapStruct
- Lombok
- Maven
- Docker Compose
- JUnit 5
- Mockito

## Requisitos

- Java 21
- Docker e Docker Compose
- Maven Wrapper, já incluído no projeto

## Como Rodar

Clone o projeto e entre na pasta do backend:

```bash
cd virtual-library-backend
```

Suba o PostgreSQL:

```bash
docker compose up -d
```

Configure as variáveis de ambiente:

```bash
JWT_SECRET=sua_chave_secreta
GOOGLE_BOOKS_API_KEY=sua_api_key_do_google_books
```

No Windows PowerShell:

```powershell
$env:JWT_SECRET="sua_chave_secreta"
$env:GOOGLE_BOOKS_API_KEY="sua_api_key_do_google_books"
```

Execute a aplicação:

```bash
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

## Banco De Dados

O projeto usa PostgreSQL em desenvolvimento. O `docker-compose.yml` cria um banco com as configurações abaixo:

```text
Database: virtual_library
User: postgres
Password: admin123
Port: 5432
```

As tabelas são versionadas com Flyway em:

```text
src/main/resources/db/migrations
```

## Autenticação

Rotas públicas:

```http
POST /auth/register
POST /auth/login
```

Exemplo de cadastro:

```json
{
  "name": "Jane Doe",
  "username": "janedoe",
  "email": "jane@example.com",
  "password": "123456"
}
```

Todo cadastro público cria usuários com role `USER`. A criação de usuários `ADMIN` não é permitida pelo endpoint público.

Exemplo de login:

```json
{
  "username": "janedoe",
  "password": "123456"
}
```

Resposta:

```json
{
  "token": "jwt-token"
}
```

Use o token nas próximas requisições:

```http
Authorization: Bearer jwt-token
```

## Principais Endpoints

### Livros

```http
POST /books
GET /books
GET /books/{bookId}
PUT /books/{bookId}
DELETE /books/{bookId}
GET /books/search
GET /books/external/search?query=clean-code
POST /books/import
```

As rotas de escrita em `/books` exigem role `ADMIN`.

### Usuário Autenticado

```http
GET /users/me
PATCH /users/me
DELETE /users/me
```

### Biblioteca Do Usuário

```http
POST /users/me/books
GET /users/me/books
PATCH /users/me/books/{bookId}
DELETE /users/me/books/{bookId}
```

## Swagger/OpenAPI

Depois de subir a aplicação, a documentação interativa fica disponível em:

```text
http://localhost:8080/swagger-ui.html
```

O contrato OpenAPI em JSON fica disponível em:

```text
http://localhost:8080/v3/api-docs
```

Para testar rotas protegidas no Swagger UI:

1. Faça login em `POST /auth/login`.
2. Copie o token retornado.
3. Clique em `Authorize`.
4. Informe o token no formato `Bearer seu-token`.

## Testes

Execute os testes com:

```bash
./mvnw test
```

No Windows:

```powershell
.\mvnw.cmd test
```

Os testes usam profile `test`, banco H2 em memória e Flyway desabilitado para evitar dependência do PostgreSQL local.

## Status Atual

Implementado:

- Autenticação e geração de JWT.
- Cadastro público seguro com role `USER`.
- CRUD de catálogo de livros.
- Busca local de livros.
- Integração com Google Books.
- Importação de livros externos.
- Biblioteca pessoal do usuário.
- Migrations com Flyway.
- Testes unitários para camada de service.
- Documentação interativa com Swagger/OpenAPI.

Em evolução:

- Testes de controller.
- Paginação e filtros avançados.
- Reviews de livros.
- Wishlist.
- Profiles separados para `dev`, `test` e `prod`.
- Pipeline de CI.

## Git Flow

- `main`: produção
- `develop`: desenvolvimento
- `feature/*`: novas funcionalidades
- `bugfix/*`: correções
