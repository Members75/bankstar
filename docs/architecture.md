## Архитектура приложения

### Диаграмма компонентов

```mermaid
graph TD
  User[Пользователь] -->|GET /recommendation/{id}| API[Spring Boot API]
  Manager[Менеджер] -->|POST/GET/DELETE /rule| API
  External[Внешняя система] -->|POST /management/cache/reset| API

  subgraph "Spring Boot Application"
    API -->|JdbcTemplate| MainDB[(PostgreSQL: transactions, stats)]
    API -->|JPA| RulesDB[(PostgreSQL: dynamic_rules)]
    API --> Cache[(Caffeine Cache)]
    API --> Telegram[(Telegram Bot)]
  end

  MainDB <--> Liquibase
  RulesDB <--> Liquibase

flowchart TD
  Start([Начало: getRecommendationsForUser(userId)]) --> LoadStatic[Получить статические правила (код)]
  LoadStatic --> EvalStatic[Оценить статические правила для userId]
  EvalStatic --> LoadDynamic[Получить динамические правила из RulesDB]
  LoadDynamic --> Deserialize[Десериализовать условия правил]
  Deserialize --> EvalDynamic[Оценить динамические правила для userId]
  EvalDynamic --> Combine[Объединить результаты: static + dynamic]
  Combine --> ApplyCache[Применить кэш статистики (если есть)]
  ApplyCache --> Filter[Отфильтровать дубликаты и недопустимые продукты]
  Filter --> Return([Вернуть список рекомендаций])