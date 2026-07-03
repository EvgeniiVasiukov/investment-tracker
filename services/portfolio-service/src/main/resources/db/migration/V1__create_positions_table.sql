CREATE TABLE IF NOT EXISTS positions (
                       id BIGSERIAL PRIMARY KEY,
                       user_id BIGINT NOT NULL,
                       ticker VARCHAR(255) NOT NULL,
                       quantity NUMERIC(19,6) NOT NULL,
                       average_price NUMERIC(6,2) NOT NULL,
                       currency VARCHAR(50) NOT NULL,
                       created_at TIMESTAMP NOT NULL
);