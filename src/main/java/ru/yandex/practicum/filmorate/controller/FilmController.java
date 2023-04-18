package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j

public class FilmController {
    private final Map<Integer, Film> filmsMap = new HashMap<>();
    private static int filmId = 1;
    private final int maxDescriptionLength = 200;
    private final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(filmsMap.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validateFilm(film);
        film.setId(filmId);
        filmsMap.put(filmId, film);
        filmId++;
        log.info("Фильм " + film.getName() + " добавлен в коллекцию");
        return film;
    }


    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        int id = film.getId();
        if (!filmsMap.containsKey(id)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Фильм не найден");
        } else {
            filmsMap.replace(id, film);
            log.info("Фильм обновлен" + film);
        }
        return film;
    }

    @ResponseBody
    public void validateFilm(Film film) {
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
