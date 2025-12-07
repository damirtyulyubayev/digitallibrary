-- Add level and teacher role support
ALTER TABLE library_users ADD COLUMN IF NOT EXISTS level VARCHAR(50) DEFAULT 'BRONZE';
UPDATE library_users SET level = COALESCE(level, 'BRONZE');

-- Add active flag to book copies
ALTER TABLE book_copies ADD COLUMN IF NOT EXISTS active BOOLEAN DEFAULT TRUE;
UPDATE book_copies SET active = TRUE WHERE active IS NULL;

-- Reservation queue
CREATE TABLE IF NOT EXISTS reservation_queue (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES library_users(id),
    book_id BIGINT REFERENCES books(id),
    position INT,
    created_at TIMESTAMP NOT NULL
);

-- Club meetings and messages
CREATE TABLE IF NOT EXISTS club_meetings (
    id BIGSERIAL PRIMARY KEY,
    club_id BIGINT REFERENCES reading_clubs(id),
    title VARCHAR(255),
    scheduled_at TIMESTAMP,
    location VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS club_messages (
    id BIGSERIAL PRIMARY KEY,
    club_id BIGINT REFERENCES reading_clubs(id),
    user_id BIGINT REFERENCES library_users(id),
    content VARCHAR(2000) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

-- Study groups and group reading lists
CREATE TABLE IF NOT EXISTS study_groups (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    institution VARCHAR(255),
    teacher_id BIGINT REFERENCES library_users(id)
);

CREATE TABLE IF NOT EXISTS group_reading_list (
    id BIGSERIAL PRIMARY KEY,
    group_id BIGINT REFERENCES study_groups(id),
    book_id BIGINT REFERENCES books(id),
    type VARCHAR(50)
);

-- Seed teacher role as needed
UPDATE library_users SET role = 'TEACHER' WHERE role = 'TEACHER';
