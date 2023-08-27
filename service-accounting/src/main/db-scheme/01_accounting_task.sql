CREATE TABLE IF NOT EXISTS accounting_task
(
    task_id BIGSERIAL PRIMARY KEY,
    task_public_id VARCHAR(36) NOT NULL,
    title VARCHAR(128) NOT NULL,
    assignee_user_id BIGINT NOT NULL REFERENCES accounting_user(user_id),
    assign_fee NUMERIC NOT NULL,
    resolve_price NUMERIC NOT NULL,
    creation_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    UNIQUE(task_public_id)
);
