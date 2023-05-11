# java-filmorate
Template repository for Filmorate project.

![Untitled](https://github.com/Ekaterina-Kirpi/java-filmorate/assets/119094349/cbdaad1e-cb76-42c1-8e62-72cb62fcbdd5)

Сведения о таблицах:

---films Содержит информацию о фильмах.

первичный ключ id — идентификатор фильма;
name - название фильма;
description - описание фильма;
release_date - дата выхода фильма;
duration - длительность фильма;
внешний ключ rating_id (ссылается на таблицу rating_mpa) — идентификатор рейтинга.

---rating_mpa Содержит информацию о рейтингах кино.

первичный ключ id — идентификатор рейтинга;
name - название рейтинга.

---ganre Содержит информацию о жанрах кино.

первичный ключ id — идентификатор жанра;
name — название жанра.

---film_genre Содержит информацию о том какие фильмы к каким жанрам относятся. 

внешний ключ film_id (отсылает к таблице films) — идентификатор фильма;
внешний ключ genre_id (отсылает к таблице ganre) — идентификатор жанра.

---users Содержит информацию о пользователях.

первичный ключ id — идентификатор пользователя;
login - логин пользователя;
name - имя пользователя;
email - электронная почта;
birthday - день рождения.

---friendship Содержит информацию, какие пользователи являются друзьями.

внешний ключ user_id (отсылает к таблице users) — идентификатор пользователя;
внешний ключ friend_id (отсылает к таблице users) — идентификатор друга.

---likes Содержит информацию об отметках "мне нравится".

внешний ключ film_id (отсылает к таблице films) — идентификатор фильма;
внешний ключ user_id (отсылает к таблице users) — идентификатор пользователя.
