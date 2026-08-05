ALTER TABLE tb_user_books
    DROP CONSTRAINT IF EXISTS fk_tb_user_books_book;

ALTER TABLE tb_user_books
    ADD CONSTRAINT fk_tb_user_books_book
        FOREIGN KEY (book_id)
            REFERENCES tb_books (id)
            ON DELETE CASCADE;


ALTER TABLE tb_user_books
    DROP CONSTRAINT IF EXISTS fk_tb_user_books_user;

ALTER TABLE tb_user_books
    ADD CONSTRAINT fk_tb_user_books_user
        FOREIGN KEY (user_id)
            REFERENCES tb_users (id)
            ON DELETE CASCADE;


ALTER TABLE tb_reviews
    DROP CONSTRAINT IF EXISTS fk_tb_reviews_user_book;

ALTER TABLE tb_reviews
    ADD CONSTRAINT fk_tb_reviews_user_book
        FOREIGN KEY (user_book_id)
            REFERENCES tb_user_books (id)
            ON DELETE CASCADE;


ALTER TABLE tb_book_images
    DROP CONSTRAINT IF EXISTS fk_tb_book_images_review;

ALTER TABLE tb_book_images
    ADD CONSTRAINT fk_tb_book_images_review
        FOREIGN KEY (review_id)
            REFERENCES tb_reviews (id)
            ON DELETE CASCADE;


ALTER TABLE tb_wishlist_items
    DROP CONSTRAINT IF EXISTS fk_tb_wishlist_items_user;

ALTER TABLE tb_wishlist_items
    ADD CONSTRAINT fk_tb_wishlist_items_user
        FOREIGN KEY (user_id)
            REFERENCES tb_users (id)
            ON DELETE CASCADE;


ALTER TABLE tb_wishlist_items
    DROP CONSTRAINT IF EXISTS fk_tb_wishlist_items_book;

ALTER TABLE tb_wishlist_items
    ADD CONSTRAINT fk_tb_wishlist_items_book
        FOREIGN KEY (book_id)
            REFERENCES tb_books (id)
            ON DELETE SET NULL;


ALTER TABLE tb_refresh_tokens
    DROP CONSTRAINT IF EXISTS fk_tb_refresh_tokens_user;

ALTER TABLE tb_refresh_tokens
    ADD CONSTRAINT fk_tb_refresh_tokens_user
        FOREIGN KEY (user_id)
            REFERENCES tb_users (id)
            ON DELETE CASCADE;