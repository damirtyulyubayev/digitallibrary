-- Bulk seed to provide 50-100 rows per table for testing endpoints.

-- Users (adds ~49 more)
INSERT INTO library_users (username, email, phone, password, role)
SELECT 'user'||g,
       'user'||g||'@example.com',
       '+100000' || LPAD(g::text, 4, '0'),
       '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5CiZ4/V4drQEL6YUtG/0m2eG7g8ua', -- bcrypt for 'password'
       CASE WHEN g % 20 = 0 THEN 'ADMIN' WHEN g % 7 = 0 THEN 'LIBRARIAN' ELSE 'READER' END
FROM generate_series(2, 50) g;
SELECT setval('library_users_id_seq', (SELECT MAX(id) FROM library_users));

-- Branches (adds 8 more)
INSERT INTO library_branches (name, address)
SELECT 'Branch '||g, (100+g)::text || ' Library Ave'
FROM generate_series(3, 10) g;
SELECT setval('library_branches_id_seq', (SELECT MAX(id) FROM library_branches));

-- Books (total to ~50)
INSERT INTO books (title, author, genre, description, total_pages)
SELECT 'Book '||g,
       'Author '||g,
       CASE WHEN g % 4 = 0 THEN 'Fiction' WHEN g % 4 = 1 THEN 'Science' WHEN g % 4 = 2 THEN 'History' ELSE 'Technology' END,
       'Sample description for book '||g,
       150 + (g * 5)
FROM generate_series(4, 50) g;
SELECT setval('books_id_seq', (SELECT MAX(id) FROM books));

-- Book copies: 2 copies per book
INSERT INTO book_copies (book_id, branch_id, inventory_code, available)
SELECT b.id,
       ((b.id % (SELECT COUNT(*) FROM library_branches)) + 1) AS branch_id,
       'INV-'||b.id||'-'||c.copy_no,
       (c.copy_no % 2 = 0)
FROM books b
JOIN LATERAL (SELECT generate_series(1,2) AS copy_no) c ON true
WHERE NOT EXISTS (
    SELECT 1 FROM book_copies bc
    WHERE bc.book_id = b.id AND bc.inventory_code = 'INV-'||b.id||'-'||c.copy_no
);
SELECT setval('book_copies_id_seq', (SELECT MAX(id) FROM book_copies));

-- Reservations (~50)
WITH bc AS (
    SELECT id, row_number() OVER (ORDER BY id) rn FROM book_copies ORDER BY id LIMIT 50
),
u AS (
    SELECT id, row_number() OVER (ORDER BY id) rn FROM library_users ORDER BY id LIMIT 50
)
INSERT INTO reservations (user_id, book_copy_id, status, created_at, due_at, returned_at)
SELECT u.id,
       bc.id,
       CASE WHEN bc.rn % 3 = 0 THEN 'ISSUED' WHEN bc.rn % 3 = 1 THEN 'REQUESTED' ELSE 'RETURNED' END,
       NOW() - (bc.rn || ' hours')::interval,
       NOW() + ((10 + (bc.rn % 5)) || ' days')::interval,
       CASE WHEN bc.rn % 3 = 2 THEN NOW() - ((bc.rn % 2 + 1) || ' days')::interval ELSE NULL END
FROM bc JOIN u ON u.rn = bc.rn
WHERE NOT EXISTS (SELECT 1 FROM reservations r WHERE r.book_copy_id = bc.id AND r.user_id = u.id);
SELECT setval('reservations_id_seq', (SELECT MAX(id) FROM reservations));

-- Reading list items (~50)
WITH b AS (
    SELECT id, row_number() OVER (ORDER BY id) rn FROM books ORDER BY id LIMIT 50
),
u AS (
    SELECT id, row_number() OVER (ORDER BY id) rn FROM library_users ORDER BY id LIMIT 50
)
INSERT INTO reading_list_items (user_id, book_id, type)
SELECT u.id,
       b.id,
       CASE WHEN b.rn % 4 = 0 THEN 'FINISHED' WHEN b.rn % 4 = 1 THEN 'READING' WHEN b.rn % 4 = 2 THEN 'WANT_TO_READ' ELSE 'DROPPED' END
FROM b JOIN u ON u.rn = b.rn
ON CONFLICT DO NOTHING;
SELECT setval('reading_list_items_id_seq', (SELECT MAX(id) FROM reading_list_items));

-- Reviews (~50)
WITH b AS (
    SELECT id, row_number() OVER (ORDER BY id) rn FROM books ORDER BY id LIMIT 50
),
u AS (
    SELECT id, row_number() OVER (ORDER BY id) rn FROM library_users ORDER BY id LIMIT 50
)
INSERT INTO book_reviews (user_id, book_id, rating, text, created_at)
SELECT u.id,
       b.id,
       3 + (b.rn % 3),
       'Review '||b.rn||' for book '||b.id,
       NOW() - (b.rn || ' days')::interval
FROM b JOIN u ON u.rn = b.rn
ON CONFLICT DO NOTHING;
SELECT setval('book_reviews_id_seq', (SELECT MAX(id) FROM book_reviews));

-- Library passes (~50 total)
INSERT INTO library_passes (user_id, qr_token, active)
SELECT u.id, 'QR-'||u.id||'-TOKEN', true
FROM library_users u
WHERE NOT EXISTS (SELECT 1 FROM library_passes lp WHERE lp.user_id = u.id)
AND u.id <= 50;
SELECT setval('library_passes_id_seq', (SELECT MAX(id) FROM library_passes));

-- Fines (~30)
INSERT INTO fines (user_id, reservation_id, amount, paid, created_at)
SELECT r.user_id,
       r.id,
       (r.id % 10 + 1),
       (r.id % 4 = 0),
       NOW() - (r.id || ' hours')::interval
FROM reservations r
WHERE r.id <= 30
ON CONFLICT DO NOTHING;
SELECT setval('fines_id_seq', (SELECT MAX(id) FROM fines));

-- Reading progress (~50)
WITH b AS (
    SELECT id, row_number() OVER (ORDER BY id) rn FROM books ORDER BY id LIMIT 50
),
u AS (
    SELECT id, row_number() OVER (ORDER BY id) rn FROM library_users ORDER BY id LIMIT 50
)
INSERT INTO reading_progress (user_id, book_id, current_page, total_pages, last_updated)
SELECT u.id,
       b.id,
       (b.rn * 5) % 200,
       150 + (b.rn * 5),
       NOW() - (b.rn || ' hours')::interval
FROM b JOIN u ON u.rn = b.rn
ON CONFLICT DO NOTHING;
SELECT setval('reading_progress_id_seq', (SELECT MAX(id) FROM reading_progress));

-- Reading goals (~30)
INSERT INTO reading_goals (user_id, target_books, target_pages, period_start, period_end)
SELECT u.id,
       5 + (u.id % 5),
       500 + (u.id * 10),
       CURRENT_DATE - (u.id || ' days')::interval,
       CURRENT_DATE + ((u.id % 10 + 20) || ' days')::interval
FROM library_users u
WHERE u.id <= 30
ON CONFLICT DO NOTHING;
SELECT setval('reading_goals_id_seq', (SELECT MAX(id) FROM reading_goals));

-- Achievements (~30)
INSERT INTO user_achievements (user_id, code, title, description)
SELECT u.id,
       'ACHV-'||u.id,
       'Achievement '||u.id,
       'Auto-awarded achievement for user '||u.id
FROM library_users u
WHERE u.id <= 30
ON CONFLICT DO NOTHING;
SELECT setval('user_achievements_id_seq', (SELECT MAX(id) FROM user_achievements));

-- Events (~15)
INSERT INTO library_events (branch_id, title, description, start_at, end_at, capacity)
SELECT ((g % (SELECT COUNT(*) FROM library_branches)) + 1),
       'Event '||g,
       'Sample event number '||g,
       NOW() + ((g + 1) || ' days')::interval,
       NOW() + ((g + 1) || ' days 2 hours')::interval,
       20 + g
FROM generate_series(2, 16) g
ON CONFLICT DO NOTHING;
SELECT setval('library_events_id_seq', (SELECT MAX(id) FROM library_events));

-- Event registrations (~40)
WITH e AS (
    SELECT id, row_number() OVER (ORDER BY id) rn FROM library_events ORDER BY id LIMIT 20
),
u AS (
    SELECT id, row_number() OVER (ORDER BY id) rn FROM library_users ORDER BY id LIMIT 40
)
INSERT INTO event_registrations (event_id, user_id, registered_at)
SELECT e.id,
       u.id,
       NOW() - (u.rn || ' hours')::interval
FROM e JOIN u ON (u.rn % 20) + 1 = e.rn
ON CONFLICT DO NOTHING;
SELECT setval('event_registrations_id_seq', (SELECT MAX(id) FROM event_registrations));

-- Reading clubs (~10)
INSERT INTO reading_clubs (name, description, owner_id)
SELECT 'Club '||g, 'Auto generated club '||g, ((g % 20) + 1)
FROM generate_series(2, 10) g
ON CONFLICT DO NOTHING;
SELECT setval('reading_clubs_id_seq', (SELECT MAX(id) FROM reading_clubs));
