-- Users
CREATE TABLE users (
  id            BIGSERIAL PRIMARY KEY,
  username      TEXT        NOT NULL UNIQUE,
  password      TEXT        NOT NULL,
  created_at    TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Medications
CREATE TABLE medications (
  id         BIGSERIAL PRIMARY KEY,
  user_id    BIGINT      NOT NULL REFERENCES users(id),
  name       TEXT        NOT NULL,
  form       TEXT,
  strength   TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  CONSTRAINT uq_med_user_name UNIQUE (user_id, name)
);

-- Regimens (a user takes a medication on a schedule)
CREATE TABLE regimens (
  id            BIGSERIAL PRIMARY KEY,
  user_id       BIGINT NOT NULL REFERENCES users(id),
  medication_id BIGINT NOT NULL REFERENCES medications(id),
  dose          TEXT,
  times_per_day INT,
  start_date    DATE,
  end_date      DATE,
  notes         TEXT
);

-- Intake logs (what happened vs the regimen)
CREATE TABLE intake_logs (
  id         BIGSERIAL PRIMARY KEY,
  user_id    BIGINT      NOT NULL REFERENCES users(id),
  regimen_id BIGINT      NOT NULL REFERENCES regimens(id),
  taken_at   TIMESTAMP NOT NULL,
  status     TEXT        NOT NULL CHECK (status IN ('TAKEN','MISSED','LATE')),
  notes      TEXT
);

-- Symptom types (lookup)
CREATE TABLE symptom_types (
  id          BIGSERIAL PRIMARY KEY,
  name        TEXT NOT NULL UNIQUE,
  description TEXT
);

-- Symptom entries (facts)
CREATE TABLE symptom_entries (
  id               BIGSERIAL PRIMARY KEY,
  user_id          BIGINT      NOT NULL REFERENCES users(id),
  symptom_type_id  BIGINT      NOT NULL REFERENCES symptom_types(id),
  recorded_at      TIMESTAMP NOT NULL,
  severity         INT         NOT NULL CHECK (severity BETWEEN 1 AND 10),
  notes            TEXT
);

-- Indexes for access patterns
CREATE INDEX idx_med_user_created    ON medications (user_id, created_at DESC);
CREATE INDEX idx_regimen_user_med    ON regimens (user_id, medication_id);
CREATE INDEX idx_intake_user_time    ON intake_logs (user_id, taken_at DESC);
CREATE INDEX idx_symptom_user_time   ON symptom_entries (user_id, recorded_at DESC);
