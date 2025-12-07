# Digital Library — Backend

Полнофункциональный цифровой каталог (Java 21, Spring Boot 3) с бронированиями, очередями, отзывами, списками чтения, рекомендациями (в т.ч. по истории), мероприятиями, клубами, учебными группами, штрафами, геймификацией (уровни/ачивки) и семантическим поиском (заглушка). Собирается в Docker, база поднимается через docker-compose.

## Стек
- Java 21, Spring Boot 3 (Web, Data JPA, Security)
- PostgreSQL
- Maven
- Lombok

## Быстрый старт (Docker)
```bash
docker compose up --build
```
- Поднимутся два сервиса: `db` (Postgres 15) и `app`.
- Приложение: http://localhost:8080
- БД: localhost:5432, `postgres/postgres`, база `digital_library`.
- Данные сохраняются в volume `db_data`.
- Схема и мок-данные создаются через Flyway (`db/migration/V1__init_schema.sql`, `V2__seed_data.sql`, `V3__bulk_seed.sql` ~50–100 строк на таблицу, `V4__extend_features.sql` — новые таблицы/столбцы).

## Локальный запуск (без Docker)
1. Подними Postgres и создай БД `digital_library` (или свои параметры).
2. Настрой переменные окружения (или оставь дефолты):
   - `DB_HOST` (default: `localhost`)
   - `DB_PORT` (default: `5432`)
   - `DB_NAME` (default: `digital_library`)
   - `DB_USER` (default: `postgres`)
   - `DB_PASSWORD` (default: `postgres`)
3. Запуск:
```bash
mvn spring-boot:run
# или
mvn clean package && java -jar target/digitallibrary-0.0.1-SNAPSHOT.jar
```

## Конфигурация
`src/main/resources/application.properties` использует переменные `DB_*`. Без них возьмёт дефолты выше. DDL ведёт Flyway (`spring.jpa.hibernate.ddl-auto=none`).
- Для AI-ассистента нужен `OPENAI_API_KEY` (OpenAI Chat Completions API) и, опционально, `openai.model` (по умолчанию `gpt-4o-mini`).

## Структура каталогов
```
src/main/java/kz/digital/library/
  config/        # SecurityConfig
  domain/        # сущности и enum'ы
  dto/           # DTO запросов
  repository/    # JPA репозитории
  service/       # бизнес-логика
  web/           # REST-контроллеры
```

## REST эндпоинты
- Auth: `POST /api/auth/register`, `POST /api/auth/login`
- Users: `GET /api/users/me`, `GET /api/users/{id}` (ADMIN/LIBRARIAN), `PATCH /api/users/{id}/role?role=` (ADMIN)
- Books: `GET /api/books?q=`, `GET /api/books/{id}`
- Search: `GET /api/search?q=&semantic={true|false}` (semantic — заглушка для pgvector/Elastic)
- Reservations: `POST /api/reservations`, `POST /api/reservations/{id}/issue`, `POST /api/reservations/{id}/return`, `GET /api/reservations/user/{userId}`
- Reservation queue: `POST /api/reservations/queue/book/{bookId}?userId=`, `GET /api/reservations/queue/book/{bookId}`
- Reading list: `POST /api/reading-list/{userId}/add?bookId=&type=`, `GET /api/reading-list/{userId}?type=`
- Reviews: `POST /api/reviews`, `GET /api/reviews/book/{bookId}`
- Recommendations: `GET /api/recommendations/user/{userId}`, `GET /api/recommendations/similar/{bookId}`
- Events: `GET /api/events`, `POST /api/events/{id}/register?userId=`
- Library pass: `GET /api/pass/{userId}`, `GET /api/pass/scan/{qrToken}`
- Gamification: `GET /api/gamification/{userId}/achievements`, `POST /api/gamification/{userId}/award`
- Stats: `GET /api/stats/reading/{userId}`, `GET /api/stats/city`
- Clubs: `POST /api/clubs`, `GET /api/clubs`, `POST /api/clubs/{id}/meetings`, `GET /api/clubs/{id}/meetings`, `POST /api/clubs/{id}/messages`, `GET /api/clubs/{id}/messages`
- Study groups (Teacher): `POST /api/groups`, `GET /api/groups/teacher/{teacherId}`, `POST /api/groups/{groupId}/assign?bookId=&type=`, `GET /api/groups/{groupId}/list`
- Librarian inventory: `POST /api/librarian/books`, `POST /api/librarian/books/{bookId}/copies`, `POST /api/librarian/copies/{copyId}/discard`, `GET /api/librarian/books/{bookId}/copies`
- Assistant (AI via OpenAI): `GET /api/assistant/plan?userId=&query=`

## Безопасность
HTTP Basic, пользователи в БД. При старте создаются `admin/admin` (ADMIN), `librarian/librarian` (LIBRARIAN), `teacher/teacher` (TEACHER). Регистрация создаёт READER. Доступ к управлению ролями/геймификации/инвентарём/группами ограничен ролями.

## Расширения (Phase 4–5)
- Семантический поиск (pgvector/Elastic) — заготовка в `SearchService.semanticSearch`.
- Рекомендации на ML — заглушка в `RecommendationService`.
- Геймификация и метрики — `GamificationService`, `StatsService`.

## Полезные команды
- Сборка: `mvn clean package`
- Локальный ран: `mvn spring-boot:run`
- Docker: `docker compose up --build` / `docker compose down -v`
