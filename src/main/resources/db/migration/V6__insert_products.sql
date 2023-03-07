INSERT INTO images (id, bytes, content_type, name, original_file_name, size)
VALUES (4, 131790,'image/png','file','2c56c91c-d69c-44d0-97c5-be7ea1abd8c0.png',8178),
       (5, 131790,'image/png','file','2c56c91c-d69c-44d0-97c5-be7ea1abd8c0.png',8178),
       (6, 131790,'image/png','file','2c56c91c-d69c-44d0-97c5-be7ea1abd8c0.png',8178),
       (7, 131790,'image/png','file','2c56c91c-d69c-44d0-97c5-be7ea1abd8c0.png',8178);
ALTER SEQUENCE image_seq RESTART WITH 8;

INSERT INTO products (id, available, created_date, discount, price, preview_image_id)
VALUES (1, TRUE, '2023-01-31 23:13:26.875301', 0, 12.00, 4),
       (2, TRUE, '2023-02-03 22:51:00.231878', 0, 30.00, 5),
       (3, TRUE, '2023-02-03 22:51:00.231878', 0, 30.00, 6),
       (4, TRUE, '2023-02-03 22:51:00.231878', 0, 30.00, 7);
ALTER SEQUENCE product_seq RESTART WITH 5;

INSERT INTO product_titles_translator (product_id, title, product_titles_key)
VALUES (1, 'Сідниця1', 1),
       (1, 'Skirt1', 3),
       (1, 'Юбка1', 2),

       (2, 'Сідниця2', 1),
       (2, 'Юбка2', 2),
       (2, 'Skirt2', 3),

       (3, 'Светр1', 1),
       (3, 'Sweter1', 3),
       (3, 'Кофта1', 2),

       (4, 'Светр2', 1),
       (4, 'Sweter2', 3),
       (4, 'Кофта2', 2);


INSERT INTO product_images (product_id, image_id)
VALUES (1, 4),
       (2, 5),
       (3, 6),
       (4, 7);

INSERT INTO product_categories (product_id, category_id)
VALUES (1, 1),
       (1, 3),
       (2, 1),
       (2, 3),
       (3, 1),
       (3, 2),
       (4, 1),
       (4, 2);

INSERT INTO product_attributes (product_id, attribute_id)
VALUES (1, 3),
       (1, 4),
       (2, 3),
       (2, 4),
       (3, 2),
       (3, 5),
       (4, 2),
       (4, 5);