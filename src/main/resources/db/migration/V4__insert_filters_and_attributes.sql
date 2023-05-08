INSERT INTO filters(id)
VALUES (1), --DollSize
       (2); --Material
ALTER SEQUENCE filter_seq RESTART WITH 3;

-- 1 - DollSize     Curve, Tall, MTM (Default)
-- 2 - Material     Sewn, Knitted

INSERT INTO filter_titles_translator(filter_id, title, filter_titles_key)
VALUES (1, 'DollSize', 1),
       (1, 'DollSize', 2),
       (1, 'DollSize', 3),
       (2, 'Material', 1),
       (2, 'Material', 2),
       (2, 'Material', 3);

INSERT INTO attributes(id)
VALUES (1), --DollSize     Curve
       (2), --DollSize     Tall
       (3), --DollSize     MTM (Default)
       (4), --Material     Sewn
       (5), --Material     Knitted
       (6); --DollSize     Petite
ALTER SEQUENCE attribute_seq RESTART WITH 7;

INSERT INTO filter_attributes (filter_id, attribute_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 4),
       (2, 5),
       (1, 6);

INSERT INTO attribute_titles_translator (attribute_id, title, attribute_titles_key)
VALUES (1, 'Curve', 1),
       (1, 'Curve', 2),
       (1, 'Curve', 3),
       (2, 'Tall', 1),
       (2, 'Tall', 2),
       (2, 'Tall', 3),
       (3, 'MTM', 1),
       (3, 'MTM', 2),
       (3, 'MTM', 3),
       (4, 'Sewn', 1),
       (4, 'Sewn', 2),
       (4, 'Sewn', 3),
       (5, 'Knitted', 1),
       (5, 'Knitted', 2),
       (5, 'Knitted', 3),
       (6, 'Petite', 1),
       (6, 'Petite', 2),
       (6, 'Petite', 3);