-- Нормализация схемы (ЛР2): выносим повторяющиеся данные клиента из orders
-- в таблицу customers, а данные товара из order_items - в связь с products.
-- Денормализованные колонки переносятся во внешние ключи, старые колонки
-- удаляются только после переноса данных (для уже существующих БД).

CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    address VARCHAR(500),
    phone VARCHAR(50),
    type VARCHAR(20) NOT NULL DEFAULT 'regular'
);

-- Переносим существующих клиентов из orders (тип для старых данных неизвестен -
-- он не сохранялся, поэтому берём REGULAR).
INSERT INTO customers (full_name, address, phone, type)
SELECT DISTINCT customer_full_name, customer_address, customer_phone, 'regular'
FROM orders;

-- orders: ссылка на customers вместо дублирования контактных данных.
ALTER TABLE orders ADD COLUMN customer_id BIGINT;

UPDATE orders o
SET customer_id = c.id
FROM customers c
WHERE c.full_name = o.customer_full_name
  AND c.address IS NOT DISTINCT FROM o.customer_address
  AND c.phone IS NOT DISTINCT FROM o.customer_phone;

ALTER TABLE orders ALTER COLUMN customer_id SET NOT NULL;
ALTER TABLE orders ADD CONSTRAINT fk_orders_customer
    FOREIGN KEY (customer_id) REFERENCES customers(id);

-- Legacy-колонки клиента остаются в orders до contract-шага (V4): они нужны,
-- чтобы потребители старой колонки customer_full_name (гейт ЛР3 lab3_migration)
-- не падали во время expand/backfill-миграций. Приложение их больше не пишет
-- (данные живут в customers), поэтому customer_full_name делаем nullable.
ALTER TABLE orders ALTER COLUMN customer_full_name DROP NOT NULL;

-- order_items: ссылка на products вместо дублирования названия и цены товара.
ALTER TABLE order_items ADD COLUMN product_id BIGINT;

UPDATE order_items oi
SET product_id = p.id
FROM products p
WHERE p.name = oi.product_name;

ALTER TABLE order_items ALTER COLUMN product_id SET NOT NULL;
ALTER TABLE order_items ADD CONSTRAINT fk_order_items_product
    FOREIGN KEY (product_id) REFERENCES products(id);

ALTER TABLE order_items
    DROP COLUMN product_name,
    DROP COLUMN product_price;