-- =========================
-- USERS
-- =========================
CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL
        CHECK (status IN ('active', 'inactive', 'blocked')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);


-- =========================
-- USER AUTH
-- =========================
CREATE TABLE user_auth (
    auth_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    password_hash TEXT NOT NULL,
    provider VARCHAR(20) NOT NULL
        CHECK (provider IN ('local', 'google', 'apple', 'facebook')),
    last_login_at TIMESTAMPTZ,
    password_updated_at TIMESTAMPTZ,

    CONSTRAINT fk_user_auth_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);


-- =========================
-- AUTH DEVICES
-- =========================
CREATE TABLE auth_devices (
    auth_device_id BIGSERIAL PRIMARY KEY,
    auth_id BIGINT NOT NULL,
    ip INET NOT NULL,
    device_id VARCHAR(255),
    user_agent TEXT,
    last_used_at TIMESTAMPTZ,

    CONSTRAINT fk_auth_devices_auth
        FOREIGN KEY (auth_id)
        REFERENCES user_auth(auth_id)
        ON DELETE CASCADE
);


-- =========================
-- BROKER
-- =========================
CREATE TABLE broker (
    broker_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    symbol VARCHAR(50) NOT NULL
);


-- =========================
-- STOCK
-- =========================
CREATE TABLE stock (
    stock_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    symbol VARCHAR(50) NOT NULL,
    broker_id BIGINT NOT NULL,
    close_day NUMERIC(15,6),
    last_fetch TIMESTAMPTZ,
    currency VARCHAR(3) NOT NULL,


    CONSTRAINT fk_stock_broker
        FOREIGN KEY (broker_id)
        REFERENCES broker(broker_id),
     CONSTRAINT uk_symbol_broker
        UNIQUE (symbol, broker_id)
);


-- =========================
-- PORTFOLIO
-- =========================
CREATE TABLE portfolio (
    portfolio_id BIGSERIAL PRIMARY KEY,
    stock_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    avg_price NUMERIC(15,6) NOT NULL,
    total_quantity NUMERIC(15,6) NOT NULL,

    CONSTRAINT fk_portfolio_stock
        FOREIGN KEY (stock_id)
        REFERENCES stock(stock_id),

    CONSTRAINT fk_portfolio_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id),

    CONSTRAINT uq_user_stock UNIQUE (user_id, stock_id)
);


-- =========================
-- OPERATION
-- =========================
CREATE TABLE operation (
    operation_id BIGSERIAL PRIMARY KEY,
    operation_type VARCHAR(10) NOT NULL
        CHECK (operation_type IN ('buy', 'sell')),
    quantity NUMERIC(15,6) NOT NULL,
    price NUMERIC(15,6) NOT NULL,
    portfolio_id BIGINT NOT NULL,
    fee NUMERIC(15,6) DEFAULT 0,
    tax NUMERIC(15,6) DEFAULT 0,
    total NUMERIC(15,6) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    modified_at TIMESTAMPTZ,

    CONSTRAINT fk_operation_portfolio
        FOREIGN KEY (portfolio_id)
        REFERENCES portfolio(portfolio_id)
        ON DELETE CASCADE
);


-- =========================
-- DIVIDEND
-- =========================
CREATE TABLE dividend (
    dividend_id BIGSERIAL PRIMARY KEY,
    dividend_type VARCHAR(20) NOT NULL
        CHECK (dividend_type IN ('cash', 'stock','reinvested')),
    value NUMERIC(15,6) NOT NULL,
    portfolio_id BIGINT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    modified_at TIMESTAMPTZ,
    paid_date DATE,
    currency_code VARCHAR(3) NOT NULL,
    tax NUMERIC(15,6) DEFAULT 0,
    net_value NUMERIC(15,6) NOT NULL,
    exchange_rate NUMERIC(15,6),

    CONSTRAINT fk_dividend_portfolio
        FOREIGN KEY (portfolio_id)
        REFERENCES portfolio(portfolio_id)
        ON DELETE CASCADE
);

