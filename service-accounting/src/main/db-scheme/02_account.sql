CREATE TABLE IF NOT EXISTS account
(
    account_id BIGSERIAL PRIMARY KEY,
    account_public_id VARCHAR(36) NOT NULL,
    user_id BIGINT NOT NULL REFERENCES accounting_user(user_id),
    balance NUMERIC NOT NULL,
    creation_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    UNIQUE(user_id, account_public_id)
);
