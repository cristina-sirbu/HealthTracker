CREATE INDEX IF NOT EXISTS idx_medications_user_createdat
    ON medications (user_id, created_at);