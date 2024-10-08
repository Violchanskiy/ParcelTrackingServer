# Проект Post Service

## Описание

Этот проект предоставляет RESTful сервис для управления почтовыми отделениями и посылками. Он использует Spring Boot для построения backend-сервиса, JPA для взаимодействия с базой данных и Swagger для документирования API, PostgreSQL в качестве базы данных и контейнеризировано с помощью Docker для простоты развертывания.

## Функции
- Управление почтовыми отделениями: добавление, получение и удаление почтовых отделений.

- Управление посылками: регистрация посылок, обновление статусов посылок (например, прибытие, отправление, доставка) и получение истории посылок.

- Обработка исключений: пользовательская обработка исключений для ParcelNotFoundException и PostOfficeNotFoundException.

- Документация Swagger: документация API с использованием Swagger.

## Структура проекта

- `controller`: содержит REST-контроллеры для обработки HTTP-запросов.
- `model`: содержит JPA-сущности, представляющие таблицы базы данных.
- `service`: содержит бизнес-логику для управления почтовыми отделениями и посылками.
- `exception`: содержит классы исключений и глобальный обработчик исключений.
- `config`: содержит  конфигурацию Swagger.
- `DTO`: содержит классы объектов передачи данных (DTO).

## Используемые технологии
- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Docker
- Swagger
- Maven

## Предварительные требования

- Docker
- Docker-compose
- Java 17
- Maven

## Запуск проекта

### Использование Docker

1. **Соберите Docker-образ:**

   ```sh
   docker build -t post_service .
   
2. **Запустите приложение с помощью Docker Compose**

   ```sh
   docker-compose up

### Без использования Docker
1. **Настройте базу данных PostgreSQL**

2. **Соберите проект:**

   ```sh 
   mvn clean install
   
3. **Запустите проект:**

   ```sh
   java -jar PostService.jar
   
Это запустит приложение. Приложение будет доступно по адресу http://localhost:8080.

## API Endpoints
### Endpoints для почтовых отделений

- GET /postOffice/all: Получить все почтовые отделения.
- GET /postOffice/{id}: Получить почтовое отделение по ID.
- POST /postOffice/add: Добавить новое почтовое отделение.
- DELETE /postOffice/delete/{id}: Удалить почтовое отделение по ID.

### Endpoints для посылок

- POST /parcel/register: Зарегистрировать новую посылку.
- GET /parcel/{id}: Получить посылку по ID.
- GET /parcel/status/{id}: Получить статус посылки.
- GET /parcel/all: Получить все посылки.
- POST /parcel/arrival/{id}: Обновить статус посылки на "ARRIVED".
- POST /parcel/departure/{id}: Обновить статус посылки на "DEPARTED".
- POST /parcel/deliver/{id}: Обновить статус посылки на "DELIVERED".
- POST /parcel/receive/{id}: Обновить статус посылки на "RECEIVED".
- GET /parcel/history/{id}: Получить историю посылки.
- DELETE /parcel/delete/{id}: Удалить посылку по ID.


## Обработка исключений
  Для обработки специфических ошибок используются настраиваемые исключения:

- ParcelNotFoundException: Возникает, когда посылка не найдена.
- PostOfficeNotFoundException: Возникает, когда почтовое отделение не найдено.
- Глобальный обработчик исключений предоставляется для обработки этих исключений и возврата соответствующих HTTP-статусов.

## Документация API

Для документирования API используется Swagger. После запуска приложения вы можете получить доступ к Swagger UI по адресу:

```sh
http://localhost:8080/swagger-ui/index.html
