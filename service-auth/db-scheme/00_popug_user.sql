CREATE TABLE IF NOT EXISTS popug_user
(
    user_id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(64) NOT NULL,
    beak_size int NOT NULL,
    user_role VARCHAR(32) NOT NULL,
    creation_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);
