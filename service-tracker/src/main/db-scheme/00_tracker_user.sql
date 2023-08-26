CREATE TABLE IF NOT EXISTS tracker_user
(
    user_id BIGSERIAL PRIMARY KEY,
    user_public_id VARCHAR(36) NOT NULL,
    user_name VARCHAR(64) NOT NULL,
    user_role VARCHAR(32) NOT NULL,
    creation_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    UNIQUE(user_public_id)
);
