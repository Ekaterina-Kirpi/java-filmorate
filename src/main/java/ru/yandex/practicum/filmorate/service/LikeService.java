package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.LikesStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    @Autowired
    private final LikesStorage likeStorage;

    public void addLike(long filmID, long userID) {
        likeStorage.addLike(filmID, userID);
    }

    public void deleteLike(long filmID, long userID) {
        likeStorage.deleteLike(filmID, userID);
    }

    public List<Film> getTopLikedFilms(Integer count) {
        return likeStorage.getTopLikedFilms(count);
    }
}