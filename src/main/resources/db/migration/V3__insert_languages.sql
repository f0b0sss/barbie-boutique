INSERT INTO language(id, code, title)
VALUES (1, 'uk', 'Українська'),
       (2, 'ru', 'Русский'),
       (3, 'en', 'English');

ALTER SEQUENCE language_seq RESTART WITH 4;
