INSERT INTO images (id, bytes, content_type, name, original_file_name, size)
    VALUES (1, 131790,'image/png','file','2c56c91c-d69c-44d0-97c5-be7ea1abd8c0.png',8178),
           (2, 131790,'image/png','file','2c56c91c-d69c-44d0-97c5-be7ea1abd8c0.png',8178),
           (3, 131790,'image/png','file','2c56c91c-d69c-44d0-97c5-be7ea1abd8c0.png',8178),
           (4, 131790,'image/png','file','2c56c91c-d69c-44d0-97c5-be7ea1abd8c0.png',8178),
           (5, 131790,'image/png','file','2c56c91c-d69c-44d0-97c5-be7ea1abd8c0.png',8178);

INSERT INTO categories (id, image_id, parent_category_id)
VALUES (1, 1, NULL),
       (2, 2, 1),
       (3, 3, 1),
       (4, 4, NULL),
       (5, 5, NULL);
ALTER SEQUENCE category_seq RESTART WITH 6;


INSERT INTO category_titles_translator (category_id, title, category_titles_key)
VALUES (1, 'Одяг', 1),
       (1, 'Closes', 3),
       (1, 'Одежда', 2),

       (2, 'Светри', 1),
       (2, 'Sweters', 3),
       (2, 'Кофти', 2),

       (3, 'Спідниці', 1),
       (3, 'Skirts', 3),
       (3, 'Юбки', 2),

       (4, 'Капсули', 1),
       (4, 'Capsules', 3),
       (4, 'Капсулы', 2),

       (5, 'Образи', 1),
       (5, 'Outfits', 3),
       (5, 'Образы', 2);