package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service

public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final int maxDescriptionLength = 200;
    private final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);


    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }


    public void addLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        if (userStorage.getUserById(userId) == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        if (film == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }

        film.addLike(userId);

    }


    public void removeLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        userStorage.getUserById(userId);
        if (!film.getLikes().contains(userId)) {
            log.warn("Лайк уже удален");
            throw new ValidationException(HttpStatus.OK, "Лайк уже удален");
        }
        film.removeLike(userId);
    }


    public List<Film> getTop10Films(long count) {
        return getFilms().stream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(film -> film.getLikes().size()))).limit(count).collect(Collectors.toList());
    }
}


