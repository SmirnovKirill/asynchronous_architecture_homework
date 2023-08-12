CREATE TABLE IF NOT EXISTS task
(
    task_id BIGSERIAL PRIMARY KEY,
    title VARCHAR(128) NOT NULL,
    description VARCHAR(1024) NOT NULL,
    status VARCHAR(32) NOT NULL,
    assignee_user_id BIGINT NOT NULL REFERENCES tracker_user(user_id),
    assign_fee NUMERIC NOT NULL,
    resolve_price NUMERIC NOT NULL,
    creation_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);
