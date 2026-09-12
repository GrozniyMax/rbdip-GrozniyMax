-- Contract-шаг (ЛР3): чтение переведено на first_name/last_name, а приложение
-- больше не использует ни customers.full_name, ни legacy-колонки клиента в
-- orders (данные живут в customers). Убираем все старые представления имени.

ALTER TABLE customers DROP COLUMN full_name;

ALTER TABLE orders
    DROP COLUMN customer_full_name,
    DROP COLUMN customer_address,
    DROP COLUMN customer_phone;