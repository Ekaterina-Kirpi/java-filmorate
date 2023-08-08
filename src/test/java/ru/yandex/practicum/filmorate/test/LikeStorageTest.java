package ru.yandex.practicum.filmorate.test;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.test.TestCreate.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeStorageTest {

    private final LikeDbStorage likeDbStorage;
    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void clearDb() {
        String sql = "DELETE FROM likes";
        jdbcTemplate.update(sql);
        sql = "DELETE FROM friendship";
        jdbcTemplate.update(sql);
        sql = "DELETE FROM film_genre";
        jdbcTemplate.update(sql);
        sql = "DELETE FROM films";
        jdbcTemplate.update(sql);
        sql = "DELETE FROM users";
        jdbcTemplate.update(sql);
    }

    private List<Long> getLikedUsers(int filmId) {
        String sql = "SELECT user_id " +
                "FROM likes " +
                "WHERE film_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, filmId);
    }

    @Test
    void addLikeTest() {
        User user = createUser1();
        user = userDbStorage.createUser(user);
        Film film = createFilm1();
        film = filmDbStorage.createFilm(film);
        likeDbStorage.addLike(film.getId(), user.getId());
        List<Long> likesFilm = getLikedUsers((int) film.getId());
        assertEquals(likesFilm.size(), 1);
        assertTrue(likesFilm.contains(user.getId()));
    }

    @Test
    void deleteLikeTest() {
        User user = createUser1();
        user = userDbStorage.createUser(user);
        Film film = createFilm1();
        film = filmDbStorage.createFilm(film);
        likeDbStorage.addLike(film.getId(), user.getId());
        likeDbStorage.deleteLike(film.getId(), user.getId());
        List<Long> likesFilm = getLikedUsers((int) film.getId());
        assertTrue(likesFilm.isEmpty());
    }

    @Test
    void deleteLikeWithNotExistingUser() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> likeDbStorage.deleteLike(-2, -1));
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);

    }

    @Test
    void getTopLikedFilms() {
        User user1 = createUser1();
        user1 = userDbStorage.createUser(user1);
        User user2 = createUser2();
        user2 = userDbStorage.createUser(user2);
        User user3 = createUser3();
        user3 = userDbStorage.createUser(user3);
        Film film1 = createFilm1();
        film1 = filmDbStorage.createFilm(film1);
        Film film2 = createFilm2();
        filmDbStorage.createFilm(film2);
        Film film3 = createFilm3();
        film3 = filmDbStorage.createFilm(film3);

        likeDbStorage.addLike(film3.getId(), user1.getId());
        likeDbStorage.addLike(film3.getId(), user2.getId());
        likeDbStorage.addLike(film3.getId(), user3.getId());
        likeDbStorage.addLike(film1.getId(), user1.getId());
        likeDbStorage.addLike(film1.getId(), user2.getId());

        List<Film> popularFilms = likeDbStorage.getTopLikedFilms(3);

        assertEquals(popularFilms.size(), 3);
        assertEquals(popularFilms.get(0), film3);
        assertEquals(popularFilms.get(1), film1);
    }

}