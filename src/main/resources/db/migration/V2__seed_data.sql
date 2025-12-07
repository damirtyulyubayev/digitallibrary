-- Sample users (password hash for plaintext "password")
INSERT INTO library_users (id, username, email, phone, password, role) VALUES
  (1, 'reader', 'reader@example.com', '+100000000', '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5CiZ4/V4drQEL6YUtG/0m2eG7g8ua', 'READER');

SELECT setval('library_users_id_seq', (SELECT MAX(id) FROM library_users));

-- Branches
INSERT INTO library_branches (id, name, address) VALUES
  (1, 'Central Library', '123 Main St'),
  (2, 'Riverside Branch', '45 River Rd');
SELECT setval('library_branches_id_seq', (SELECT MAX(id) FROM library_branches));

-- Books
INSERT INTO books (id, title, author, genre, description, total_pages) VALUES
  (1, 'The Pragmatic Programmer', 'Andrew Hunt', 'Technology', 'Classic on pragmatic development.', 352),
  (2, 'Clean Code', 'Robert C. Martin', 'Technology', 'Guide to writing clean code.', 464),
  (3, 'Dune', 'Frank Herbert', 'Science Fiction', 'Epic science fiction saga.', 688);
SELECT setval('books_id_seq', (SELECT MAX(id) FROM books));

-- Book copies
INSERT INTO book_copies (id, book_id, branch_id, inventory_code, available) VALUES
  (1, 1, 1, 'INV-001', true),
  (2, 1, 2, 'INV-002', false),
  (3, 3, 1, 'INV-003', true);
SELECT setval('book_copies_id_seq', (SELECT MAX(id) FROM book_copies));

-- Reservations
INSERT INTO reservations (id, user_id, book_copy_id, status, created_at, due_at, returned_at) VALUES
  (1, 1, 2, 'ISSUED', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP + INTERVAL '13 day', NULL);
SELECT setval('reservations_id_seq', (SELECT MAX(id) FROM reservations));

-- Reading list
INSERT INTO reading_list_items (id, user_id, book_id, type) VALUES
  (1, 1, 3, 'WANT_TO_READ');
SELECT setval('reading_list_items_id_seq', (SELECT MAX(id) FROM reading_list_items));

-- Reviews
INSERT INTO book_reviews (id, user_id, book_id, rating, text, created_at) VALUES
  (1, 1, 1, 5, 'Great insights on pragmatic development.', CURRENT_TIMESTAMP - INTERVAL '2 day');
SELECT setval('book_reviews_id_seq', (SELECT MAX(id) FROM book_reviews));

-- Library pass
INSERT INTO library_passes (id, user_id, qr_token, active) VALUES
  (1, 1, 'QR-READER-123', true);
SELECT setval('library_passes_id_seq', (SELECT MAX(id) FROM library_passes));

-- Fine
INSERT INTO fines (id, user_id, reservation_id, amount, paid, created_at) VALUES
  (1, 1, 1, 5.00, false, CURRENT_TIMESTAMP);
SELECT setval('fines_id_seq', (SELECT MAX(id) FROM fines));

-- Reading progress
INSERT INTO reading_progress (id, user_id, book_id, current_page, total_pages, last_updated) VALUES
  (1, 1, 1, 120, 352, CURRENT_TIMESTAMP - INTERVAL '1 hour');
SELECT setval('reading_progress_id_seq', (SELECT MAX(id) FROM reading_progress));

-- Reading goal
INSERT INTO reading_goals (id, user_id, target_books, target_pages, period_start, period_end) VALUES
  (1, 1, 3, 900, CURRENT_DATE, CURRENT_DATE + INTERVAL '1 month');
SELECT setval('reading_goals_id_seq', (SELECT MAX(id) FROM reading_goals));

-- Achievement
INSERT INTO user_achievements (id, user_id, code, title, description) VALUES
  (1, 1, 'FIRST_BOOK', 'First Book', 'Completed first reservation.');
SELECT setval('user_achievements_id_seq', (SELECT MAX(id) FROM user_achievements));

-- Events
INSERT INTO library_events (id, branch_id, title, description, start_at, end_at, capacity) VALUES
  (1, 1, 'Sci-Fi Night', 'Discussion of classic sci-fi books.', CURRENT_TIMESTAMP + INTERVAL '2 day', CURRENT_TIMESTAMP + INTERVAL '2 day 2 hour', 30);
SELECT setval('library_events_id_seq', (SELECT MAX(id) FROM library_events));

-- Event registration
INSERT INTO event_registrations (id, event_id, user_id, registered_at) VALUES
  (1, 1, 1, CURRENT_TIMESTAMP);
SELECT setval('event_registrations_id_seq', (SELECT MAX(id) FROM event_registrations));

-- Reading club
INSERT INTO reading_clubs (id, name, description, owner_id) VALUES
  (1, 'Tech Readers', 'Club for tech and sci-fi fans.', 1);
SELECT setval('reading_clubs_id_seq', (SELECT MAX(id) FROM reading_clubs));
