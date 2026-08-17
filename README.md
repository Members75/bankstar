# Recommendation Engine (BankStar)

Система выдачи персонализированных рекомендаций продуктов на основе статических и динамических правил.

## Стек технологий
- Java 17+
- Spring Boot 3.3.4
- Gradle 9.5.1
- PostgreSQL (2 БД)
- JPA + Hibernate
- Lombok
- Jackson
- Caffeine (кэш)
- Liquibase
- Swagger / OpenAPI

## Краткое описание
Сервис предоставляет рекомендации пользователям на основе:
- Статических правил (зашиты в коде как `RecommendationRuleSet`).
- Динамических правил (хранятся в отдельной БД, управляются через REST API).

Алгоритм рекомендаций комбинирует результаты обоих типов правил и возвращает единый список.

## Документация
- [Требования и Use Cases](docs/requirements.md)
- [Архитектура и диаграммы](docs/architecture.md)
- [REST API (OpenAPI)](docs/api.md)
- [Инструкция по развертыванию](docs/deployment.md)

## Ссылки
- GitHub Issues: https://github.com/Members75/bankstar
- GitHub Projects: https://github.com/users/Members75/projects/1