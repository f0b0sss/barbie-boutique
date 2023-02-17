INSERT INTO users(id, firstname, lastname, password, email, phone, role, status)
VALUES (1, 'Dmitriy', 'Zimin', '$2a$12$5ztW/j4hIpGRZ.6oUQAC5.0S.2kNjaVMETkgJP1uHGGvxwhbpMe9y',
        'admin@mail.com', '380636621800', 'ADMIN', 'ACTIVE'),
       (2, 'Liza', 'Matveykina', '$2a$12$5ztW/j4hIpGRZ.6oUQAC5.0S.2kNjaVMETkgJP1uHGGvxwhbpMe9y',
        'user@mail.com', '380636621800', 'CLIENT', 'ACTIVE');
ALTER SEQUENCE user_seq RESTART WITH 3;

INSERT INTO buckets(id, user_id)
VALUES (1, 1),
       (2, 2);
ALTER SEQUENCE user_seq RESTART WITH 3;