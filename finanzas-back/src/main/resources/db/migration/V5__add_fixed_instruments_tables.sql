-- =========================
-- FIXED INSTRUMENTS
-- =========================
CREATE TABLE fixed_instruments (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    anual_rate NUMERIC(15,6) NOT NULL
);

-- =========================
-- FIXED PORTFOLIO
-- =========================
CREATE TABLE fixed_portfolio (
    id BIGSERIAL PRIMARY KEY,
    fixed_instrument_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    amount NUMERIC(15,6) NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT false,

    CONSTRAINT fk_fixed_portfolio_instrument
        FOREIGN KEY (fixed_instrument_id)
        REFERENCES fixed_instruments(id),

    CONSTRAINT fk_fixed_portfolio_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
);

-- =========================
-- DAILY PAYS
-- =========================
CREATE TABLE daily_pays (
    id BIGSERIAL PRIMARY KEY,
    fixed_portfolio_id BIGINT NOT NULL,
    amount NUMERIC(15,6) NOT NULL,
    anual_rate_calculated NUMERIC(15,6) NOT NULL,

    CONSTRAINT fk_daily_pays_portfolio
        FOREIGN KEY (fixed_portfolio_id)
        REFERENCES fixed_portfolio(id)
        ON DELETE CASCADE
);
