# java-filmorate
Бэкенд для сервиса, который будет работать с фильмами и оценками пользователей, а также возвращать топ фильмов, рекомендованных к просмотру.

Технологии: Java + Spring Boot + Maven + Lombok + JUnit + RESTful API + JDBC

![Untitled (2)](https://github.com/Ekaterina-Kirpi/java-filmorate/assets/119094349/08c33e50-839a-42ca-a03f-781c486b2df3)



Реализованы следующие эндпоинты:
1. Фильмы


POST /films - создание фильма

PUT /films - редактирование фильма

GET /films - получение списка всех фильмов

GET /films/{id} - получение информации о фильме по его id

PUT /films/{id}/like/{userId} — поставить лайк фильму

DELETE /films/{id}/like/{userId} — удалить лайк фильма

GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков. Если значение параметра count не задано, возвращает первые 10.

2. Пользователи

   
POST /users - создание пользователя

PUT /users - редактирование пользователя

GET /users - получение списка всех пользователей

GET /users/{id} - получение данных о пользователе по id

PUT /users/{id}/friends/{friendId} — добавление в друзья

DELETE /users/{id}/friends/{friendId} — удаление из друзей

GET /users/{id}/friends — возвращает список друзей

GET /users/{id}/friends/common/{otherId} — возвращает список друзей, общих с другим пользователем










