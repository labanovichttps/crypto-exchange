--liquibase formatted sql

--changeset LabanovichTsimafei:create_users_table
CREATE TABLE IF NOT EXISTS users
(
    id               BIGSERIAL PRIMARY KEY,
    username         VARCHAR UNIQUE NOT NULL,
    password         VARCHAR        NOT NULL,
    created_datetime TIMESTAMPTZ    NOT NULL,
    update_datetime  TIMESTAMPTZ
);

--changeset LabanovichTsimafei:wallet
CREATE TABLE IF NOT EXISTS wallet
(
    id               BIGSERIAL PRIMARY KEY,
    address          VARCHAR UNIQUE               NOT NULL,
    user_id          BIGINT REFERENCES users (id) NOT NULL,
    created_datetime TIMESTAMPTZ                  NOT NULL
);

--changeset LabanovichTsimafei:balance
CREATE TABLE IF NOT EXISTS balance
(
    id               BIGSERIAL PRIMARY KEY,
    amount           DECIMAL                       NOT NULL,
    currency         VARCHAR                       NOT NULL,
    wallet_id        BIGINT REFERENCES wallet (id) NOT NULL,
    created_datetime TIMESTAMPTZ                   NOT NULL,
    update_datetime  TIMESTAMPTZ
);

--changeset LabanovichTsimafei:orders
CREATE TABLE IF NOT EXISTS orders
(
    id               BIGSERIAL PRIMARY KEY,
    type             VARCHAR                      NOT NULL,
    status           VARCHAR                      NOT NULL,
    kind             VARCHAR                      NOT NULL,
    amount           DECIMAL                      NOT NULL,
    currency         VARCHAR                      NOT NULL,
    user_id          BIGINT REFERENCES users (id) NOT NULL,
    created_datetime TIMESTAMPTZ                  NOT NULL
);
