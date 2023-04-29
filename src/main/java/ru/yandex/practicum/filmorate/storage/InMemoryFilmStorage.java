package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> filmsMap = new HashMap<>();
    private static long filmId = 1;
    private final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);


    @Override
    public Film createFilm(Film film) {
        validateFilm(film);
        film.setId(filmId);
        filmsMap.put(film.getId(), film);
        filmId++;
        log.info("Фильм " + film.getName() + " добавлен в коллекцию");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        long id = film.getId();
        if (!filmsMap.containsKey(id)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Фильм не найден");
        } else {
            filmsMap.replace(id, film);
            log.info("Фильм обновлен" + film);
            return film;
        }
    }

    @Override
    public List<Film> getFilms() {
        log.info("Список фильмов получен");
        return new ArrayList<>(filmsMap.values());
    }

    @Override
    public Film getFilmById(long id) {
        if (!filmsMap.containsKey(id)) {
            log.error("Фильма не существует");
            throw new ValidationException(HttpStatus.NOT_FOUND, "Фильма не существует");
        }
        return filmsMap.get(id);
    }

    @ResponseBody
    public void validateFilm(Film film) {
        int maxDescriptionLength = 200;
        if (filmsMap.containsKey(film.getId())) {
            log.warn("Такой фильм уже есть в коллекции");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Такой фильм уже есть в коллекции");
        } else if (film.getReleaseDate().isBefore(minReleaseDate)) {
            log.warn("Неверно указана дата выпуска");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Неверно указана дата выпуска");
        } else if (film.getDescription().length() > maxDescriptionLength) {
            log.warn("Максимальная длина описания - 200 символов");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Максимальная длина описания - 200 символов");
        } else if (film.getDuration() < 0) {
            log.warn("Продолжительность фильма должна быть положительной");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Продолжительность фильма должна быть положительной");
        }
    }

}