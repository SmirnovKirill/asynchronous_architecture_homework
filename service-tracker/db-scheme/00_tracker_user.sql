CREATE TABLE IF NOT EXISTS tracker_user
(
    user_id BIGINT PRIMARY KEY,
    user_name VARCHAR(64) NOT NULL,
    user_role VARCHAR(32) NOT NULL,
    creation_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);
