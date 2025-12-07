-- Users
CREATE TABLE library_users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255),
    phone VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Books and branches
CREATE TABLE books (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    genre VARCHAR(255),
    description VARCHAR(2000),
    total_pages INT
);

CREATE TABLE library_branches (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL
);

CREATE TABLE book_copies (
    id BIGSERIAL PRIMARY KEY,
    book_id BIGINT REFERENCES books(id),
    branch_id BIGINT REFERENCES library_branches(id),
    inventory_code VARCHAR(255) NOT NULL UNIQUE,
    available BOOLEAN NOT NULL
);

-- Reservations
CREATE TABLE reservations (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES library_users(id),
    book_copy_id BIGINT REFERENCES book_copies(id),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    due_at TIMESTAMP,
    returned_at TIMESTAMP
);

-- Reading lists
CREATE TABLE reading_list_items (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES library_users(id),
    book_id BIGINT REFERENCES books(id),
    type VARCHAR(50) NOT NULL,
    CONSTRAINT reading_list_unique UNIQUE (user_id, book_id)
);

-- Reviews
CREATE TABLE book_reviews (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES library_users(id),
    book_id BIGINT REFERENCES books(id),
    rating INT NOT NULL,
    text VARCHAR(2000),
    created_at TIMESTAMP NOT NULL
);

-- Library pass
CREATE TABLE library_passes (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE REFERENCES library_users(id),
    qr_token VARCHAR(255) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL
);

-- Fines
CREATE TABLE fines (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES library_users(id),
    reservation_id BIGINT REFERENCES reservations(id),
    amount NUMERIC(12,2) NOT NULL,
    paid BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL
);

-- Reading progress & goals
CREATE TABLE reading_progress (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES library_users(id),
    book_id BIGINT REFERENCES books(id),
    current_page INT,
    total_pages INT,
    last_updated TIMESTAMP
);

CREATE TABLE reading_goals (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES library_users(id),
    target_books INT,
    target_pages INT,
    period_start DATE,
    period_end DATE
);

-- Achievements
CREATE TABLE user_achievements (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES library_users(id),
    code VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    CONSTRAINT achievement_unique UNIQUE (user_id, code)
);

-- Events & registrations
CREATE TABLE library_events (
    id BIGSERIAL PRIMARY KEY,
    branch_id BIGINT REFERENCES library_branches(id),
    title VARCHAR(255) NOT NULL,
    description VARCHAR(2000),
    start_at TIMESTAMP NOT NULL,
    end_at TIMESTAMP NOT NULL,
    capacity INT
);

CREATE TABLE event_registrations (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT REFERENCES library_events(id),
    user_id BIGINT REFERENCES library_users(id),
    registered_at TIMESTAMP NOT NULL,
    CONSTRAINT event_registration_unique UNIQUE (event_id, user_id)
);

-- Reading clubs
CREATE TABLE reading_clubs (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(2000),
    owner_id BIGINT REFERENCES library_users(id)
);
