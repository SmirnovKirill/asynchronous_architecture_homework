CREATE TABLE IF NOT EXISTS account_log
(
    account_log_id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL REFERENCES account(account_id),
    operation_type VARCHAR(32) NOT NULL,
    amount NUMERIC NOT NULL,
    operation_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    extra_ids JSONB NOT NULL
);
