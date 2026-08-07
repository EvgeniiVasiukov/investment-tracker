CREATE TABLE IF NOT EXISTS transactions (
                                         id BIGSERIAL PRIMARY KEY,
                                         user_id BIGINT NOT NULL,
                                         transaction_type VARCHAR(255) NOT NULL,
                                         ticker VARCHAR(255) NOT NULL,
                                         quantity NUMERIC(19,6) NOT NULL,
                                         price NUMERIC(19,6) NOT NULL,
                                         currency VARCHAR(50) NOT NULL,
                                         fees NUMERIC(19,6) NOT NULL,
                                         tax NUMERIC(19,6) NOT NULL,
                                         transaction_date TIMESTAMP NOT NULL
);