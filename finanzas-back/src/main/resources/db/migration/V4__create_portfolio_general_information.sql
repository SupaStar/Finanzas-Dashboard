CREATE TABLE portfolio_general_information (
    portfolio_general_information_id BIGSERIAL PRIMARY KEY,
    portfolio_id BIGINT NOT NULL,
    year INT NOT NULL,
    month INT NOT NULL,
    dividends_total NUMERIC(15,6) DEFAULT 0,

    CONSTRAINT fk_portfolio_gen_info_portfolio
        FOREIGN KEY (portfolio_id)
        REFERENCES portfolio(portfolio_id)
        ON DELETE CASCADE,

    CONSTRAINT uq_portfolio_year_month
        UNIQUE (portfolio_id, year, month)
);

ALTER TABLE portfolio ADD COLUMN n_operations INT DEFAULT 0;
