CREATE TABLE sync_state (
    sync_key VARCHAR(50) PRIMARY KEY,
    last_id_processed BIGINT NOT NULL
);

CREATE TABLE dividend_announcements (
    id SERIAL PRIMARY KEY,
    source_id BIGINT NOT NULL UNIQUE,
    ticker VARCHAR(50) NOT NULL,
    company VARCHAR(255),
    amount NUMERIC(19, 4) NOT NULL,
    comment VARCHAR(500),
    type VARCHAR(50) NOT NULL,
    pay_date DATE,
    ex_date DATE,
    link VARCHAR(1000)
);
