# README.md
___
🚨 ВНИМАНИЕ! 🚨

Это не настоящий интернет-магазин, а учебный проект-симулятор, созданный исключительно в образовательных целях. Здесь нельзя купить:

Космические корабли

Драконов

Бесконечный запас пиццы

Или что-то реальное (вообще ничего)

Все совпадения с реальными магазинами случайны, все товары вымышлены, а деньги принимаются только в воображаемой валюте.
___

## 1. Краткое описание проекта

**myShop** - это веб-приложение интернет-магазина, разработанное на Spring Boot. Приложение предоставляет функционал для просмотра товаров, управления корзиной покупок и оформления заказов. Основные возможности включают:
- Просмотр каталога товаров с фильтрацией и пагинацией
- Детальная страница товара
- Управление корзиной (добавление/удаление товаров, изменение количества)
- Оформление заказов
- Просмотр истории заказов

## 2. Используемый стек технологий

### Основные технологии:
- **Java 21** - язык программирования
- **Spring Boot 3.4.6** - фреймворк для создания приложения
- **Spring MVC** - для веб-слоя
- **Thymeleaf** - шаблонизатор для UI
- **Spring Data JPA** + **Hibernate** - для работы с базой данных
- **Liquibase** - для управления миграциями базы данных

### Базы данных:
- **PostgreSQL** - основная база данных (production)
- **H2** - in-memory база для тестирования

### Дополнительные библиотеки:
- **Lombok** - для сокращения boilerplate кода
- **MapStruct** - для маппинга DTO
- **Apache Commons Lang** - утилиты для работы со строками
- **Spring Boot Actuator** - для мониторинга приложения

### Тестирование:
- **JUnit 5** - фреймворк для тестирования
- **Spring Boot Test** - для интеграционного тестирования

## 3. Способы запуска

### Запуск в среде разработки

1. Клонировать репозиторий:
   ```bash
   git clone https://https://github.com/raaCodeCat/myShop.git
   ```

2. Убедитесь, что у вас установлены:
    - JDK 21
    - Maven
    - PostgreSQL (или измените настройки на H2 в `application-dev.yml`)

3. Соберите проект:
   ```bash
   mvn clean install
   ```

4. Запустите приложение:
   ```bash
   mvn spring-boot:run
   ```

5. Приложение будет доступно по адресу: http://localhost:8090

### Запуск с помощью Docker

1. Убедитесь, что у вас установлены Docker и Docker Compose

2. Соберите JAR-файл:
   ```bash
   mvn clean package
   ```

3. Запустите сервисы с помощью Docker Compose:
   ```bash
   docker-compose up -d
   ```

4. Приложение будет доступно по адресу: http://localhost:8090

### Docker Compose конфигурация

Пример `docker-compose.yml`:
```yaml
version: '3.1'
services:
  myshop-service:
    build:
      context: ./
      dockerfile: Dockerfile
    container_name: myshop-service
    ports:
      - "8090:8090"
    depends_on:
      myshop-db:
        condition: service_healthy

  myshop-db:
    image: postgres:16-alpine
    container_name: myshop-db
    ports:
      - "5435:5432"
    environment:
      - POSTGRES_DB=myShop
      - POSTGRES_USER=ДОБАВИТЬ_ВАШЕГО_ПОЛЬЗОВАТЕЛЯ
      - POSTGRES_PASSWORD=ДОБАВИТЬ_ВАШ_ПАРОЛЬ
    healthcheck:
      test: [ "CMD-SHELL", "pg_isready -U shopUser -d myShop" ]
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  postgres_data:
```

### Пример Dockerfile

```dockerfile
FROM amazoncorretto:21
COPY target/*.jar appMyShop.jar
ENTRYPOINT ["java","-jar","/appMyShop.jar"]
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://myshop-db:5432/myShop
ENV POSTGRES_USER=ДОБАВИТЬ_ВАШЕГО_ПОЛЬЗОВАТЕЛЯ
ENV POSTGRES_PASSWORD=ДОБАВИТЬ_ВАШ_ПАРОЛЬ
```

Для остановки приложения:
```bash
docker-compose down
```
