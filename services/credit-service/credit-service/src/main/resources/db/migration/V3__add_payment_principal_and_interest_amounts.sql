ALTER TABLE credit_payments
ADD COLUMN  principal_amount NUMERIC(19,2) NOT NULL,
    ADD COLUMN interest_amount NUMERIC(19,2) NOT NULL;