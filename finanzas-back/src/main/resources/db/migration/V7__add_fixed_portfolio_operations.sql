CREATE TABLE fixed_portfolio_operation (
    id BIGSERIAL PRIMARY KEY,
    fixed_portfolio_id BIGINT NOT NULL,
    amount NUMERIC(15, 6) NOT NULL,
    operation_type VARCHAR(20) NOT NULL,
    operation_date TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_fixed_portfolio_operation_portfolio
        FOREIGN KEY (fixed_portfolio_id)
        REFERENCES fixed_portfolio(id)
);
