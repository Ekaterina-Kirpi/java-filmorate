package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikesStorage {
    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    List<Film> getTopLikedFilms(long count);
}