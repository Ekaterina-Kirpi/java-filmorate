package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.LikesStorage;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;


    public void addLike(long filmID, long userID) {
        String sql = "INSERT INTO likes (film_id, user_id) " +
                "VALUES (?,?)";
        try {
            jdbcTemplate.update(sql, filmID, userID);
        } catch (DataAccessException exception) {
            throw new ValidationException(HttpStatus.OK, "Лайк уже поставлен");
        }

    }

    public void deleteLike(long filmID, long userID) {
        String sql = "DELETE FROM likes " +
                "WHERE film_id = ? AND user_id = ?";
        int count = jdbcTemplate.update(sql, filmID, userID);
        if (count == 0) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Такого фильма нет");
        }
    }

    public List<Film> getTopLikedFilms(long count) {
        String sql = "SELECT films.*, mpa.name AS mpa_name " +
                "FROM films " +
                "LEFT JOIN likes ON films.id=likes.film_id " +
                "JOIN rating_mpa AS mpa ON mpa.rating_id = films.rating_id " +
                "GROUP BY films.id " +
                "ORDER BY COUNT(likes.user_id) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, filmRowMapper, count);
    }

}