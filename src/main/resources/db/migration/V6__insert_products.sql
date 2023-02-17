INSERT INTO images (id, bytes, content_type, name, original_file_name, size)
VALUES (8, 85826, 'image/png', 'files', 'gratis-png-logotipo-de-barbie-muneca-mattel-yak.png', 14973),
       (9, 85827, 'image/png', 'file', 'ie_logo_PNG11.png', 17395),
       (10, 94022, 'image/jpeg', 'file', 'лого.jpg', 213295),
       (11, 85826, 'image/png', 'files', 'gratis-png-logotipo-de-barbie-muneca-mattel-yak.png', 14973),
       (12, 85827, 'image/png', 'file', 'ie_logo_PNG11.png', 17395),
       (13, 94022, 'image/jpeg', 'file', 'лого.jpg', 213295),
       (14, 85826, 'image/png', 'files', 'gratis-png-logotipo-de-barbie-muneca-mattel-yak.png', 14973),
       (15, 85827, 'image/png', 'file', 'ie_logo_PNG11.png', 17395),
       (16, 94022, 'image/jpeg', 'file', 'лого.jpg', 213295),
       (17, 94023, 'image/jpeg', 'files', 'зображення_viber_2022-12-13_00-08-59-784.jpg', 96114),
       (18, 94022, 'image/jpeg', 'file', 'лого.jpg', 213295),
       (19, 94022, 'image/jpeg', 'file', 'лого.jpg', 213295);
ALTER SEQUENCE image_seq RESTART WITH 20;

INSERT INTO products (id, available, created_date, discount, price, preview_image_id)
VALUES (1, TRUE, '2023-01-31 23:13:26.875301', 0, 12.00, 8),
       (2, TRUE, '2023-02-03 22:51:00.231878', 0, 30.00, 11),
       (3, TRUE, '2023-02-03 22:51:00.231878', 0, 30.00, 14),
       (4, TRUE, '2023-02-03 22:51:00.231878', 0, 30.00, 17);
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
VALUES (1, 8),
       (1, 9),
       (1, 10),

       (2, 11),
       (2, 12),
       (2, 13),

       (3, 14),
       (3, 15),
       (3, 16),

       (4, 17),
       (4, 18),
       (4, 19);

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