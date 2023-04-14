package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
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
    private boolean isFilmValid = false;
    private int MAX_DESCRIPTION_LENGTH = 200;
    private LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);

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
            throw new ValidationException("Фильм не найден");
        } else {
            filmsMap.replace(id, film);
            log.info("Фильм обновлен" + film.toString());
        }
        return film;
    }

    public void validateFilm(Film film) {
        if (filmsMap.containsKey(film.getId())) {
            log.warn("Такой фильм уже есть в коллекции");
            throw new ValidationException("Такой фильм уже есть в коллекции");
        } else if (film.getReleaseDate().isBefore(minReleaseDate)) {
            log.warn("Неверно указана дата выпуска");
            throw new ValidationException("Неверно указана дата выпуска");
        } else if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.warn("Максимальная длина описания - 200 символов");
            throw new ValidationException("Максимальная длина описания - 200 символов");
        } else if (film.getDuration() < 0) {
            log.warn("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }

    }
}
