package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(long id);

    List<Film> getFilms();

    Film getFilmById(long id);

    List<Film> loadFilmGenres(List<Film> films);

    Film loadFilmGenre(Film film);

    void deleteFilmGenre(long id);

    void updateFilmGenre(Film film);

    void setFilmGenre(Film film);


}