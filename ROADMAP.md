# Roadmap da API de Biblioteca Virtual

Este arquivo organiza os principais conceitos, melhorias e implementacoes que podem ser aplicados na API. A ideia e usar como checklist de evolucao do projeto.

## 1. Testes

- [X] Ativar o profile `test` no `VirtualLibraryApplicationTests`.
- [X] Garantir que os testes usem H2, nao PostgreSQL local.
- [X] Criar testes unitarios para `BookService`.
- [ ] Criar testes unitarios para `UserBookService`.
- [X] Completar testes do `UserService`.
- [ ] Criar testes de controller para `/auth`.
- [ ] Criar testes de controller para `/books`.
- [ ] Criar testes de controller para `/users/me`.
- [ ] Criar testes de controller para `/users/me/books`.

## 2. HTTP e REST

- [X] Retornar `201 Created` em criacao: `POST /auth/register`, `POST /books`, `POST /users/me/books`.
- [X] Retornar `204 No Content` em deletes: `DELETE /users/me`, `DELETE /books/{bookId}`, `DELETE /users/me/books/{bookId}`.
- [X] Evitar respostas em texto puro, como `"Book updated successfully"`.
- [X] Padronizar responses de sucesso com DTOs.
- [X] Padronizar responses de erro com `RestErrorMessage`.
- [X] Rever `GET /users/me/books` vazio: preferir `200 []` em vez de `404`.

## 3. Validacao

- [ ] Adicionar validacoes completas em `RegisterDTO`.
- [X] Adicionar validacoes em `AuthenticationDTO`.
- [ ] Validar `UserBookUpdateDTO`.
- [ ] Validar datas: `finishedAt` nao pode ser antes de `startedAt`.
- [ ] Criar regra: se status for `FINISHED`, `finishedAt` deve existir.
- [ ] Criar regra: se status for `READING`, `startedAt` deve existir.
- [ ] Tratar erros de validacao no `GlobalExceptionHandler`.

## 4. Seguranca

- [ ] Garantir que usuario comum nao consiga criar `ADMIN` no `/auth/register`.
- [ ] Criar uma regra para primeiro admin ou seed manual.
- [ ] Revisar permissoes de `/books`.
- [ ] Validar token expirado com resposta clara.
- [ ] Melhorar `SecurityFilter` usando constructor injection.
- [ ] Evitar `defaultSecretKey` em producao.
- [ ] Criar profiles para `dev`, `test` e `prod`.

## 5. Banco e Dados

- [ ] Adicionar indices em campos buscados: `title`, `author`, `genre`, `isbn`, `username`, `email`.
- [ ] Avaliar `UNIQUE` para `isbn`.
- [ ] Melhorar migration da tabela de livros.
- [ ] Criar migration para indices.
- [ ] Revisar `ddl-auto=validate`.
- [ ] Estudar transacoes com `@Transactional`.
- [ ] Adicionar `@Transactional` nos metodos de escrita dos services.

## 6. Books

- [ ] Criar paginacao em `GET /books`.
- [ ] Criar paginacao em `GET /books/search`.
- [ ] Melhorar busca para aceitar filtro unico geral: `GET /books/search?q=harry`.
- [ ] Retornar DTO em todos endpoints.
- [ ] Melhorar `BookSearchResponseDTO` ou unificar com `BookResponseDTO`.
- [ ] Adicionar validacao contra ISBN duplicado.
- [ ] Criar testes para create, update, delete e search.

## 7. User Books

- [ ] Manter `GET /users/me/books` retornando lista vazia se nao houver livros.
- [ ] Criar filtro por status: `GET /users/me/books?status=READING`.
- [ ] Criar filtro por formato: `GET /users/me/books?format=PDF`.
- [ ] Criar estatisticas: `GET /users/me/books/stats`.
- [ ] Validar duplicidade com `BookAlreadyInLibraryException`.
- [ ] Criar testes para adicionar, listar, atualizar e remover livro.

## 8. Reviews

- [ ] Criar `ReviewRepository`.
- [ ] Criar `ReviewService`.
- [ ] Criar `ReviewController`.
- [ ] Criar endpoint: `POST /users/me/books/{bookId}/review`.
- [ ] Criar endpoint: `PUT /users/me/books/{bookId}/review`.
- [ ] Criar endpoint: `DELETE /users/me/books/{bookId}/review`.
- [ ] Criar endpoint: `GET /books/{bookId}/reviews`.
- [ ] Validar nota entre 1 e 5.
- [ ] Permitir apenas uma review por livro do usuario.

## 9. Wishlist

- [ ] Criar `WishlistRepository`.
- [ ] Criar `WishlistService`.
- [ ] Criar `WishlistController`.
- [ ] Criar endpoint: `POST /users/me/wishlist`.
- [ ] Criar endpoint: `GET /users/me/wishlist`.
- [ ] Criar endpoint: `DELETE /users/me/wishlist/{itemId}`.
- [ ] Criar endpoint para converter wishlist em livro da biblioteca.
- [ ] Criar testes.

## 10. Documentacao

- [ ] Adicionar Swagger/OpenAPI.
- [ ] Documentar endpoints de auth.
- [ ] Documentar endpoints de books.
- [ ] Documentar endpoints de user books.
- [ ] Adicionar exemplos de request/response.
- [ ] Melhorar `README.md` com:
  - como rodar
  - como configurar banco
  - como autenticar
  - exemplos de chamadas

## 11. DevOps

- [ ] Criar `docker-compose.yml` com PostgreSQL.
- [ ] Criar profile `dev`.
- [ ] Criar profile `test`.
- [ ] Criar profile `prod`.
- [ ] Criar `.env.example`.
- [ ] Criar pipeline GitHub Actions para rodar testes.
- [ ] Garantir que a API sobe do zero com README.

## Ordem Recomendada

1. Testes com H2.
2. HTTP e status code.
3. Validacoes.
4. Seguranca do registro/admin.
5. Paginacao em livros.
6. Filtros e stats da biblioteca do usuario.
7. Reviews.
8. Wishlist.
9. Swagger.
10. Docker.

