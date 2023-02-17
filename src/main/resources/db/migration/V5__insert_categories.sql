INSERT INTO images (id, bytes, content_type, name, original_file_name, size)
VALUES (1, 85826, 'image/png', 'files', 'gratis-png-logotipo-de-barbie-muneca-mattel-yak.png', 14973),
       (2, 85827, 'image/png', 'file', 'ie_logo_PNG11.png', 17395),
       (3, 94022, 'image/jpeg', 'file', 'лого.jpg', 213295),
       (4, 94023, 'image/jpeg', 'files', 'зображення_viber_2022-12-13_00-08-59-784.jpg', 96114),
       (7, 94026, 'image/jpeg', 'files', 'logo2.jpg', 265585);



INSERT INTO categories (id, image_id, parent_category_id)
VALUES (1, 1, NULL),
       (2, 2, 1),
       (3, 3, 1);
ALTER SEQUENCE category_seq RESTART WITH 4;



INSERT INTO category_titles_translator (category_id, title, category_titles_key)
VALUES (1, 'Одяг', 1),
       (1, 'Closes', 3),
       (1, 'Одежда', 2),

       (2, 'Светри', 1),
       (2, 'Sweters', 3),
       (2, 'Кофти', 2),

       (3, 'Спідниці', 1),
       (3, 'Skirts', 3),
       (3, 'Юбки', 2);