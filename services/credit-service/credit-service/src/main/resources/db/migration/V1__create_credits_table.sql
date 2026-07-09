CREATE TABLE credits (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    bank_name VARCHAR(255),
    principal_amount NUMERIC(19,2),
    annual_interest_rate NUMERIC(5,2) NOT NULL,
    term_months INTEGER NOT NULL,
    monthly_payment NUMERIC(19,2) NOT NULL,
    start_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);