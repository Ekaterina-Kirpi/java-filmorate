package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final int maxDescriptionLength = 200;
    private final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);


    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film createFilm(Film film) {
        Film newFilm = filmStorage.createFilm(film);
        filmStorage.setFilmGenre(newFilm);
        return newFilm;
    }

    public Film updateFilm(Film film) {
        Film newFilm = filmStorage.updateFilm(film);
        filmStorage.updateFilmGenre(newFilm);
        return film;
    }

    public List<Film> getFilms() {

        return filmStorage.loadFilmGenres(filmStorage.getFilms());
    }

    public Film getFilmById(long id) {

        return filmStorage.loadFilmGenre(filmStorage.getFilmById(id));
    }


    public List<Film> getTopFilms(long count) {
        return getFilms().stream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(film -> film.getLikes().size()))).limit(count).collect(Collectors.toList());
    }
}


