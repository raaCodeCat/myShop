DROP VIEW IF EXISTS order_details;
DROP TABLE IF EXISTS orderitems;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS orders;

CREATE TABLE IF NOT EXISTS items (
     id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
     name VARCHAR(50) NOT NULL,
     description VARCHAR(1024) NOT NULL,
     price NUMERIC(10, 2) NOT NULL,
     image_path VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    is_paid BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS orderitems (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id),
    item_id BIGINT NOT NULL REFERENCES items(id),
    count INTEGER NOT NULL,
    UNIQUE (order_id, item_id)
);

CREATE OR REPLACE VIEW order_details AS
SELECT
    o.id as order_id,
    i.id as item_id,
    i.name as title,
    i.description,
    i.price,
    oi.count,
    i.price * oi.count as total,
    o.is_paid,
    o.user_id,
    i.image_path
FROM orderitems oi
         INNER JOIN orders o ON o.id = oi.order_id
         LEFT JOIN items i ON i.id = oi.item_id;

INSERT INTO items (name, description, price, image_path) VALUES
    ('Тестовый товар 1', 'Описание 1', 100.00, 'img1.jpg'),
    ('Тестовый товар 2', 'Описание 2', 200.00, 'img2.jpg'),
    ('Умный гаджет', 'Описание 3', 300.00, 'img3.jpg');

INSERT INTO orders (user_id, is_paid) VALUES
    (1, false);

INSERT INTO orderitems (order_id, item_id, count) VALUES
    (1, 1, 2);
