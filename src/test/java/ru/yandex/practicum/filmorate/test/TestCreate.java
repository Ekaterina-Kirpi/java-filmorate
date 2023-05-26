package ru.yandex.practicum.filmorate.test;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class TestCreate {
    public static final Genre[] genres = {
            new Genre(1, "Комедия"),
            new Genre(2, "Драма"),
            new Genre(3, "Мультфильм"),
            new Genre(4, "Триллер"),
            new Genre(5, "Документальный"),
            new Genre(6, "Боевик")
    };

    public static final Mpa[] ratings = {
            new Mpa(1, "G"),
            new Mpa(2, "PG"),
            new Mpa(3, "PG-13'"),
            new Mpa(4, "R"),
            new Mpa(5, "NC-17"),
    };

    public static Film createFilm1() {
        Film film = new Film("Первый Фильм", "Описание 1", LocalDate.now().minusYears(2), 120);
        film.setMpa(ratings[0]);
        return film;
    }

    public static Film createFilm2() {
        Film film = new Film("Второй Фильм", "Описание 2", LocalDate.now().minusYears(2), 120);
        film.setMpa(ratings[1]);
        return film;
    }

    public static Film createFilm3() {
        Film film = new Film("Третий Фильм", "Описание 3", LocalDate.now().minusYears(2), 120);
        film.setMpa(ratings[2]);
        return film;
    }

    public static User createUser1() {
        return new User("login1", "Первый", "1@mail.ru", LocalDate.now().minusYears(2));
    }

    public static User createUser2() {
        return new User("login2", "Второй", "2@mail.ru", LocalDate.now().minusYears(2));
    }

    public static User createUser3() {
        return new User("login3", "Третий", "3@mail.ru", LocalDate.now().minusYears(2));
    }

}

