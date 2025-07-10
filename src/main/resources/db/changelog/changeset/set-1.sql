/*
* Database Change Log - SQL Format
* Version: 1.1
* Author: Mynthon
* Date: 05.07.2025
* Изменение колонки в таблице users
* Упорядоченные по наборам изменений, соответствующим формату Liquibase
* Всего 1 операция
*/

ALTER TABLE users ALTER COLUMN patronymic DROP NOT NULL;

CREATE INDEX idx_users_phone_number ON users(phone_number);
CREATE INDEX idx_users_first_name ON users(first_name);
CREATE INDEX idx_users_last_name ON users(last_name);