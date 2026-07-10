CREATE TABLE credit_payments (
    id BIGSERIAL PRIMARY KEY,
    credit_id BIGINT NOT NULL,
    payment_date DATE NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    payment_type VARCHAR(55) NOT NULL,
    payment_source VARCHAR(55) NOT NULL ,
    created_at TIMESTAMP NOT NULL ,
    CONSTRAINT
        fk_credit_payments_credit
                             FOREIGN KEY (credit_id)
                             REFERENCES credits(id)
                         ON DELETE CASCADE
);