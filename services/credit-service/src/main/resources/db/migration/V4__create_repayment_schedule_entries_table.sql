CREATE TABLE repayment_schedule_entries (
    id BIGSERIAL PRIMARY KEY,
    credit_id BIGINT NOT NULL,
    installment_number BIGINT NOT NULL,
    payment_date DATE NOT NULL,
    total_payment_amount NUMERIC(19,2) NOT NULL,
    principal_amount NUMERIC(19,2) NOT NULL,
    interest_amount NUMERIC(19,2) NOT NULL,
    remaining_principal_amount NUMERIC(19,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT
    fk_repayment_schedule_entries_credit
    FOREIGN KEY (credit_id)
    REFERENCES credits(id)
);