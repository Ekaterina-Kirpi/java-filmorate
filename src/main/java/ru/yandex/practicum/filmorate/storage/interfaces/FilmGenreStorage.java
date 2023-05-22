package ru.yandex.practicum.filmorate.storage.interfaces;

public interface FilmGenreStorage {
    void  addGenreToFilm(long filmId, long genreId);
    void removeGenreFromFilm(long filmId);


}
