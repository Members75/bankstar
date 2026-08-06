
Проект реализует MVP рекомендательной системы по ТЗ банка «Стар». Разработка ведётся по Git Flow: ветка `dev` — активная разработка, ветка `main` — стабильная версия. Текущие изменения предложены через Pull Request из `dev` в `main`.

## Технологии
- Spring Boot
- Java 17+
- Gradle
- JdbcTemplate (без JPA/Hibernate)
- H2 (режим READ_ONLY)

## Как запустить
1. Убедись, что файл базы данных `transaction.mv.db` лежит в корне проекта.
2. Запусти приложение:
    - Linux/macOS: `./gradlew bootRun`
    - Windows: `gradlew.bat bootRun`
3. Проверь эндпоинт: `GET http://localhost:8080/recommendation/{user_id}`

## Примеры запросов
Используй эти ID пользователей из ТЗ — для них должны сработать правила:

```bash
curl http://localhost:8080/recommendation/cd515076-5d8a-44be-930e-8d4fcb79f42d
curl http://localhost:8080/recommendation/d4a4d619-9a0c-4fc5-b0cb-76c49409546b
curl http://localhost:8080/recommendation/1f9b149c-6577-448a-bc94-16bea229b71a