-- V2: Deletando as colunas 'rating' e 'start_date' da tabela 'tb_user_books'

ALTER TABLE tb_user_books DROP COLUMN IF EXISTS rating;
ALTER TABLE tb_user_books DROP COLUMN IF EXISTS start_date;