/*
* Database Change Log - SQL Format
* Version: 1.1
* Author: Mynthon
* Date: 10.07.2025
* Изменение колонки в таблице users и добавление индексов
* Упорядоченные по наборам изменений, соответствующим формату Liquibase
* Всего 4 операции
*/

ALTER TABLE users ALTER COLUMN patronymic DROP NOT NULL;

CREATE INDEX idx_users_phone_number ON users(phone_number);
CREATE INDEX idx_users_first_name ON users(first_name);
CREATE INDEX idx_users_last_name ON users(last_name);