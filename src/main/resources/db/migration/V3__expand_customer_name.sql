-- Expand-шаг (ЛР3): добавляем first_name/last_name для клиента, старую
-- колонку full_name пока оставляем (период dual-write). Трансформация имени
-- выполняется в приложении (CustomerMapper), SQL-бэкфилл намеренно не
-- используется, поэтому колонки добавляются nullable.

ALTER TABLE customers ADD COLUMN first_name VARCHAR(255);
ALTER TABLE customers ADD COLUMN last_name VARCHAR(255);