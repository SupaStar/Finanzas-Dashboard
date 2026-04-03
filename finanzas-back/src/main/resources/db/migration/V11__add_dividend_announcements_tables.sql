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

CREATE TABLE notifications (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    link VARCHAR(1000),
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);
