/*
* Database Change Log - SQL Format
* Version: 1.0
* Author: Mynthon
* Date: 05.07.2025
* Инициализация схемы и таблиц
* Упорядоченные по наборам изменений, соответствующим формату Liquibase
* Всего 1 схема и 3 таблицы
*/

CREATE TABLE bank_schema.users(
   id UUID PRIMARY KEY NOT NULL,
   first_name VARCHAR(100) NOT NULL,
   last_name VARCHAR(100) NOT NULL,
   patronymic VARCHAR(100),
   password VARCHAR(255) NOT NULL,
   phone_number VARCHAR(20) NOT NULL UNIQUE,
   register_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE bank_schema.roles(
  id UUID PRIMARY KEY NOT NULL,
  role_type VARCHAR(10) NOT NULL,
  user_id UUID NOT NULL,
  CONSTRAINT fk_authorities_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE bank_schema.cards(
 id UUID PRIMARY KEY NOT NULL,
 number_card VARCHAR(55) NOT NULL UNIQUE,
 hash_card_number VARCHAR(255) NOT NULL UNIQUE,
 payment_system VARCHAR(55) NOT NULL,
 score NUMERIC(15,2) NOT NULL,
 validity_period_from TIMESTAMP NOT NULL,
 validity_period_to TIMESTAMP NOT NULL,
 is_active BOOLEAN NOT NULL,
 user_id UUID NOT NULL,
 CONSTRAINT fk_cards_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE bank_schema.cards_transfers(
 id UUID PRIMARY KEY NOT NULL,
 user_id UUID NOT NULL,
 source_hash_card VARCHAR(255) NOT NULL,
 target_hash_card VARCHAR(255) NOT NULL,
 amount NUMERIC(15,2) NOT NULL,
 error_message VARCHAR(255),
 transfer_time TIMESTAMP NOT NULL,
 status_transfer VARCHAR(55) NOT NULL
);


CREATE INDEX idx_users_phone_number ON users(phone_number);
CREATE INDEX idx_users_first_name ON users(first_name);
CREATE INDEX idx_users_last_name ON users(last_name);
CREATE INDEX idx_cards_user_id ON cards(user_id);
CREATE INDEX idx_cards_validity_period ON cards(validity_period_from, validity_period_to);
CREATE INDEX idx_cards_active ON cards(is_active);
CREATE INDEX idx_transfers_status ON cards_transfers(status_transfer);
CREATE INDEX idx_transfers_source ON cards_transfers(source_hash_card);
CREATE INDEX idx_transfers_target ON cards_transfers(target_hash_card);
CREATE INDEX idx_transfers_time ON cards_transfers(transfer_time);
CREATE INDEX idx_roles_user_id ON roles(user_id);