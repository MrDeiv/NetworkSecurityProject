CREATE TABLE IF NOT EXISTS tokens (
    token TEXT NOT NULL,
    user_id TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (token, user_id)
);
