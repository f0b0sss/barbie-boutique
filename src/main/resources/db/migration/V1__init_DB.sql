CREATE SEQUENCE attribute_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE bucket_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE category_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE comment_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE filter_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE image_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE language_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE order_details_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE orders_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE outfit_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE product_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE token_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE user_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE attribute_titles_translator
(
    attribute_id         bigint NOT NULL,
    title                varchar(255),
    attribute_titles_key bigint NOT NULL,
    PRIMARY KEY (attribute_id, attribute_titles_key)
);
CREATE TABLE attributes
(
    id bigint NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE buckets
(
    id      bigint NOT NULL,
    user_id bigint,
    PRIMARY KEY (id)
);
CREATE TABLE buckets_products
(
    bucket_id  bigint NOT NULL,
    product_id bigint NOT NULL
);
CREATE TABLE categories
(
    id                 bigint NOT NULL,
    image_id           bigint,
    parent_category_id bigint,
    PRIMARY KEY (id)
);
CREATE TABLE category_titles_translator
(
    category_id         bigint NOT NULL,
    title               varchar(255),
    category_titles_key bigint NOT NULL,
    PRIMARY KEY (category_id, category_titles_key)
);
CREATE TABLE comments
(
    id         bigint NOT NULL,
    text       varchar(255),
    product_id bigint,
    user_id    bigint,
    PRIMARY KEY (id)
);
CREATE TABLE filter_attributes
(
    filter_id    bigint NOT NULL,
    attribute_id bigint NOT NULL
);
CREATE TABLE filter_titles_translator
(
    filter_id         bigint NOT NULL,
    title             varchar(255),
    filter_titles_key bigint NOT NULL,
    PRIMARY KEY (filter_id, filter_titles_key)
);
CREATE TABLE filters
(
    id bigint NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE images
(
    id                 bigint NOT NULL,
    bytes              oid,
    content_type       varchar(255),
    name               varchar(255),
    original_file_name varchar(255),
    size               bigint,
    PRIMARY KEY (id)
);
CREATE TABLE language
(
    id    bigint NOT NULL,
    code  varchar(255),
    title varchar(255),
    PRIMARY KEY (id)
);
CREATE TABLE order_details
(
    id         bigint NOT NULL,
    price      numeric(38, 2),
    product_id bigint,
    PRIMARY KEY (id)
);
CREATE TABLE orders
(
    id      bigint NOT NULL,
    created timestamp(6),
    status  varchar(255),
    sum     numeric(38, 2),
    updated timestamp(6),
    user_id bigint,
    PRIMARY KEY (id)
);
CREATE TABLE orders_details
(
    order_id  bigint NOT NULL,
    detail_id bigint NOT NULL
);
CREATE TABLE outfit_images
(
    outfit_id bigint NOT NULL,
    image_id  bigint NOT NULL
);
CREATE TABLE outfit_products
(
    product_id bigint NOT NULL,
    outfit_id  bigint NOT NULL
);
CREATE TABLE outfits
(
    id               bigint NOT NULL,
    preview_image_id bigint,
    price            numeric(38, 2),
    PRIMARY KEY (id)
);
CREATE TABLE product_attributes
(
    product_id   bigint NOT NULL,
    attribute_id bigint NOT NULL
);
CREATE TABLE product_categories
(
    product_id  bigint NOT NULL,
    category_id bigint NOT NULL
);
CREATE TABLE product_images
(
    product_id bigint NOT NULL,
    image_id   bigint NOT NULL
);
CREATE TABLE product_titles_translator
(
    product_id         bigint NOT NULL,
    title              varchar(255),
    product_titles_key bigint NOT NULL,
    PRIMARY KEY (product_id, product_titles_key)
);
CREATE TABLE products
(
    id               bigint       NOT NULL,
    available        boolean      NOT NULL,
    created_date     timestamp(6) NOT NULL,
    discount         integer      NOT NULL,
    price            numeric(38, 2),
    preview_image_id bigint,
    PRIMARY KEY (id)
);
CREATE TABLE token
(
    id          bigint NOT NULL,
    expiry_date timestamp(6),
    token       varchar(255),
    user_id     bigint NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE users
(
    id        bigint NOT NULL,
    email     varchar(255),
    enabled   boolean,
    firstname varchar(255),
    lastname  varchar(255),
    password  varchar(255),
    phone     varchar(255),
    role      varchar(255),
    status    varchar(255),
    PRIMARY KEY (id)
);
ALTER TABLE IF EXISTS filter_attributes
    ADD CONSTRAINT UK_4mugernl9utcw56sp2xddabte UNIQUE (attribute_id);
ALTER TABLE IF EXISTS orders_details
    ADD CONSTRAINT UK_6sksyv2vdyrar27lqfv3esnky UNIQUE (detail_id);
ALTER TABLE IF EXISTS outfit_images
    ADD CONSTRAINT UK_fav00mhnbmkpytcxcmdkyy9eu UNIQUE (image_id);
ALTER TABLE IF EXISTS product_images
    ADD CONSTRAINT UK_faiw41ddc6nywa21m1nodqvy5 UNIQUE (image_id);
ALTER TABLE IF EXISTS attribute_titles_translator
    ADD CONSTRAINT FKampc32swy3s8gi2v38nxwiq2o FOREIGN KEY (attribute_titles_key) REFERENCES language;
ALTER TABLE IF EXISTS attribute_titles_translator
    ADD CONSTRAINT FKeed9yh7ff6l9bbw3y0jn89nql FOREIGN KEY (attribute_id) REFERENCES attributes;
ALTER TABLE IF EXISTS buckets
    ADD CONSTRAINT FKnl0ltaj67xhydcrfbq8401nvj FOREIGN KEY (user_id) REFERENCES users;
ALTER TABLE IF EXISTS buckets_products
    ADD CONSTRAINT FKloyxdc1uy11tayedf3dpu9lci FOREIGN KEY (product_id) REFERENCES products;
ALTER TABLE IF EXISTS buckets_products
    ADD CONSTRAINT FKc49ah45o66gy2f2f4c3os3149 FOREIGN KEY (bucket_id) REFERENCES buckets;
ALTER TABLE IF EXISTS categories
    ADD CONSTRAINT FKqhmw54g2p4xu0k71mblvlqfvi FOREIGN KEY (image_id) REFERENCES images;
ALTER TABLE IF EXISTS categories
    ADD CONSTRAINT FK9il7y6fehxwunjeepq0n7g5rd FOREIGN KEY (parent_category_id) REFERENCES categories;
ALTER TABLE IF EXISTS category_titles_translator
    ADD CONSTRAINT FKh7tkgjbubijjxv3l7s2yi9533 FOREIGN KEY (category_titles_key) REFERENCES language;
ALTER TABLE IF EXISTS category_titles_translator
    ADD CONSTRAINT FK40n6id30j5an6b4jj64jxbchd FOREIGN KEY (category_id) REFERENCES categories;
ALTER TABLE IF EXISTS comments
    ADD CONSTRAINT FK6uv0qku8gsu6x1r2jkrtqwjtn FOREIGN KEY (product_id) REFERENCES products;
ALTER TABLE IF EXISTS comments
    ADD CONSTRAINT FK8omq0tc18jd43bu5tjh6jvraq FOREIGN KEY (user_id) REFERENCES users;
ALTER TABLE IF EXISTS filter_attributes
    ADD CONSTRAINT FK2qvh6l26leywbumgsifdmdmew FOREIGN KEY (attribute_id) REFERENCES attributes;
ALTER TABLE IF EXISTS filter_attributes
    ADD CONSTRAINT FKpoxolfqju98ru490v67cfiw39 FOREIGN KEY (filter_id) REFERENCES filters;
ALTER TABLE IF EXISTS filter_titles_translator
    ADD CONSTRAINT FKa0a8oqtrhgseysbtd3mwr7mbp FOREIGN KEY (filter_titles_key) REFERENCES language;
ALTER TABLE IF EXISTS filter_titles_translator
    ADD CONSTRAINT FK79a45x7hy5ljjun7v0dtorwmc FOREIGN KEY (filter_id) REFERENCES filters;
ALTER TABLE IF EXISTS order_details
    ADD CONSTRAINT FK4q98utpd73imf4yhttm3w0eax FOREIGN KEY (product_id) REFERENCES products;
ALTER TABLE IF EXISTS orders
    ADD CONSTRAINT FK32ql8ubntj5uh44ph9659tiih FOREIGN KEY (user_id) REFERENCES users;
ALTER TABLE IF EXISTS orders_details
    ADD CONSTRAINT FK9sr9upt9o7x1oxer4ymb9eppi FOREIGN KEY (detail_id) REFERENCES order_details;
ALTER TABLE IF EXISTS orders_details
    ADD CONSTRAINT FK5o977kj2vptwo70fu7w7so9fe FOREIGN KEY (order_id) REFERENCES orders;
ALTER TABLE IF EXISTS outfit_images
    ADD CONSTRAINT FKssg0hnt6w9vee5uikx6nl1idt FOREIGN KEY (image_id) REFERENCES images;
ALTER TABLE IF EXISTS outfit_images
    ADD CONSTRAINT FKrgkcqdv7vk2keh9aid5i9th1a FOREIGN KEY (outfit_id) REFERENCES outfits;
ALTER TABLE IF EXISTS outfit_products
    ADD CONSTRAINT FKonqy6qxqsg6tkbdut3dso5cn4 FOREIGN KEY (outfit_id) REFERENCES products;
ALTER TABLE IF EXISTS outfit_products
    ADD CONSTRAINT FK1pdr772dj4cgh6sy2cjd576mq FOREIGN KEY (product_id) REFERENCES outfits;
ALTER TABLE IF EXISTS product_attributes
    ADD CONSTRAINT FK6ksuorb5567jpa08ihcumumy1 FOREIGN KEY (attribute_id) REFERENCES attributes;
ALTER TABLE IF EXISTS product_attributes
    ADD CONSTRAINT FKcex46yvx4g18b2pn09p79h1mc FOREIGN KEY (product_id) REFERENCES products;
ALTER TABLE IF EXISTS product_categories
    ADD CONSTRAINT FKd112rx0alycddsms029iifrih FOREIGN KEY (category_id) REFERENCES categories;
ALTER TABLE IF EXISTS product_categories
    ADD CONSTRAINT FKlda9rad6s180ha3dl1ncsp8n7 FOREIGN KEY (product_id) REFERENCES products;
-- ALTER TABLE IF EXISTS product_images
--     ADD CONSTRAINT FK1j9bvqvvdudsd1ydm4fr0y3bk FOREIGN KEY (image_id) REFERENCES images;
ALTER TABLE IF EXISTS product_images
    ADD CONSTRAINT FKqnq71xsohugpqwf3c9gxmsuy FOREIGN KEY (product_id) REFERENCES products;
ALTER TABLE IF EXISTS product_titles_translator
    ADD CONSTRAINT FK5olltnll9nec79edbua8a0mxd FOREIGN KEY (product_titles_key) REFERENCES language;
ALTER TABLE IF EXISTS product_titles_translator
    ADD CONSTRAINT FK5utk7e8g99p75wlbokmqiyh9u FOREIGN KEY (product_id) REFERENCES products;
ALTER TABLE IF EXISTS products
    ADD CONSTRAINT FKb45ssns0b0258ie8jpiyx532n FOREIGN KEY (preview_image_id) REFERENCES images;
ALTER TABLE IF EXISTS token
    ADD CONSTRAINT FKj8rfw4x0wjjyibfqq566j4qng FOREIGN KEY (user_id) REFERENCES users;