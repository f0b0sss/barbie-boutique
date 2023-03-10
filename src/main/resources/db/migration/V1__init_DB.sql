CREATE SEQUENCE address_seq START 1 INCREMENT 1;
CREATE SEQUENCE attribute_seq START 1 INCREMENT 1;
CREATE SEQUENCE bucket_seq START 1 INCREMENT 1;
CREATE SEQUENCE category_seq START 1 INCREMENT 1;
CREATE SEQUENCE comment_seq START 1 INCREMENT 1;
CREATE SEQUENCE filter_seq START 1 INCREMENT 1;
CREATE SEQUENCE image_seq START 1 INCREMENT 1;
CREATE SEQUENCE language_seq START 1 INCREMENT 1;
CREATE SEQUENCE order_details_seq START 1 INCREMENT 1;
CREATE SEQUENCE orders_seq START 1 INCREMENT 1;
CREATE SEQUENCE outfit_seq START 1 INCREMENT 1;
CREATE SEQUENCE product_seq START 1 INCREMENT 1;
CREATE SEQUENCE user_seq START 1 INCREMENT 1;
CREATE TABLE addresses
(
    id        int8 NOT NULL,
    apartment int4 NOT NULL,
    city      varchar(255),
    country   varchar(255),
    street    varchar(255),
    user_id   int8,
    PRIMARY KEY (id)
);
CREATE TABLE attribute_titles_translator
(
    attribute_id         int8 NOT NULL,
    title                varchar(255),
    attribute_titles_key int8 NOT NULL,
    PRIMARY KEY (attribute_id, attribute_titles_key)
);
CREATE TABLE attributes
(
    id int8 NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE buckets
(
    id      int8 NOT NULL,
    user_id int8,
    PRIMARY KEY (id)
);
CREATE TABLE buckets_products
(
    bucket_id  int8 NOT NULL,
    product_id int8 NOT NULL
);
CREATE TABLE categories
(
    id                 int8 NOT NULL,
    image_id           int8,
    parent_category_id int8,
    PRIMARY KEY (id)
);
CREATE TABLE category_titles_translator
(
    category_id         int8 NOT NULL,
    title               varchar(255),
    category_titles_key int8 NOT NULL,
    PRIMARY KEY (category_id, category_titles_key)
);
CREATE TABLE comments
(
    id   int8 NOT NULL,
    text varchar(255),
    PRIMARY KEY (id)
);
CREATE TABLE filter_attributes
(
    filter_id    int8 NOT NULL,
    attribute_id int8 NOT NULL
);
CREATE TABLE filter_titles_translator
(
    filter_id         int8 NOT NULL,
    title             varchar(255),
    filter_titles_key int8 NOT NULL,
    PRIMARY KEY (filter_id, filter_titles_key)
);
CREATE TABLE filters
(
    id int8 NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE images
(
    id                 int8 NOT NULL,
    bytes              oid,
    content_type       varchar(255),
    name               varchar(255),
    original_file_name varchar(255),
    size               int8,
    PRIMARY KEY (id)
);
CREATE TABLE language
(
    id    int8 NOT NULL,
    code  varchar(255),
    title varchar(255),
    PRIMARY KEY (id)
);
CREATE TABLE order_details
(
    id         int8 NOT NULL,
    price      numeric(19, 2),
    product_id int8,
    PRIMARY KEY (id)
);
CREATE TABLE orders
(
    id      int8 NOT NULL,
    created timestamp,
    status  varchar(255),
    sum     numeric(19, 2),
    updated timestamp,
    user_id int8,
    PRIMARY KEY (id)
);
CREATE TABLE orders_details
(
    order_id  int8 NOT NULL,
    detail_id int8 NOT NULL
);
CREATE TABLE outfit_images
(
    outfit_id int8 NOT NULL,
    image_id  int8 NOT NULL
);
CREATE TABLE outfit_products
(
    product_id int8 NOT NULL,
    outfit_id  int8 NOT NULL
);
CREATE TABLE outfits
(
    id               int8 NOT NULL,
    preview_image_id int8,
    price            numeric(19, 2),
    PRIMARY KEY (id)
);
CREATE TABLE product_attributes
(
    product_id   int8 NOT NULL,
    attribute_id int8 NOT NULL
);
CREATE TABLE product_categories
(
    product_id  int8 NOT NULL,
    category_id int8 NOT NULL
);
CREATE TABLE product_comments
(
    product_id int8 NOT NULL,
    comment_id int8 NOT NULL
);
CREATE TABLE product_images
(
    product_id int8 NOT NULL,
    image_id   int8 NOT NULL
);
CREATE TABLE product_titles_translator
(
    product_id         int8 NOT NULL,
    title              varchar(255),
    product_titles_key int8 NOT NULL,
    PRIMARY KEY (product_id, product_titles_key)
);
CREATE TABLE products
(
    id               int8      NOT NULL,
    available        boolean   NOT NULL,
    created_date     timestamp NOT NULL,
    discount         int4      NOT NULL,
    price            numeric(19, 2),
    preview_image_id int8,
    PRIMARY KEY (id)
);
CREATE TABLE user_comments
(
    comment_id int8,
    user_id    int8 NOT NULL,
    PRIMARY KEY (user_id)
);
CREATE TABLE users
(
    id        int8 NOT NULL,
    email     varchar(255),
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
ALTER TABLE IF EXISTS product_comments
    ADD CONSTRAINT UK_7dnyrxxvbpwihesaqbxni8isq UNIQUE (comment_id);
ALTER TABLE IF EXISTS product_images
    ADD CONSTRAINT UK_faiw41ddc6nywa21m1nodqvy5 UNIQUE (image_id);
ALTER TABLE IF EXISTS addresses
    ADD CONSTRAINT FK1fa36y2oqhao3wgg2rw1pi459 FOREIGN KEY (user_id) REFERENCES users;
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
ALTER TABLE IF EXISTS product_comments
    ADD CONSTRAINT FK8qgeec6agwxy422iqf9dxm0u4 FOREIGN KEY (comment_id) REFERENCES comments;
ALTER TABLE IF EXISTS product_comments
    ADD CONSTRAINT FKlvw9kwav1pell1wg6xo0dmme6 FOREIGN KEY (product_id) REFERENCES products;
ALTER TABLE IF EXISTS product_images
    ADD CONSTRAINT FK1j9bvqvvdudsd1ydm4fr0y3bk FOREIGN KEY (image_id) REFERENCES images;
ALTER TABLE IF EXISTS product_images
    ADD CONSTRAINT FKqnq71xsohugpqwf3c9gxmsuy FOREIGN KEY (product_id) REFERENCES products;
ALTER TABLE IF EXISTS product_titles_translator
    ADD CONSTRAINT FK5olltnll9nec79edbua8a0mxd FOREIGN KEY (product_titles_key) REFERENCES language;
ALTER TABLE IF EXISTS product_titles_translator
    ADD CONSTRAINT FK5utk7e8g99p75wlbokmqiyh9u FOREIGN KEY (product_id) REFERENCES products;
ALTER TABLE IF EXISTS products
    ADD CONSTRAINT FKb45ssns0b0258ie8jpiyx532n FOREIGN KEY (preview_image_id) REFERENCES images;
ALTER TABLE IF EXISTS user_comments
    ADD CONSTRAINT FKot0wphbhmvik0yib4agch632b FOREIGN KEY (comment_id) REFERENCES users;
ALTER TABLE IF EXISTS user_comments
    ADD CONSTRAINT FKe3s47o2hhw8lek5clp4lyxiou FOREIGN KEY (user_id) REFERENCES comments;