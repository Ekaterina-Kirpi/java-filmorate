package ru.yandex.practicum.filmorate.test;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.test.TestCreate.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void deleteDb() {
        String sql = "DELETE FROM film_genre";
        jdbcTemplate.update(sql);
        sql = "DELETE FROM films";
        jdbcTemplate.update(sql);
    }

   @Test
    void getAllFilms() {
        Film film1 = createFilm1();
        Film film2 = createFilm2();
        filmDbStorage.createFilm(film1);
        filmDbStorage.createFilm(film2);
        Collection<Film> films = filmDbStorage.getFilms();
        Assertions.assertEquals(films.size(), 2);
        Assertions.assertTrue(films.contains((film1)));
        Assertions.assertTrue(films.contains(film2));

    }


    @Test
    void getFilmBadId() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmDbStorage.getFilmById(-1));
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
    }

   @Test
    void addFilm() {
        Film film = createFilm1();
        film = filmDbStorage.createFilm(film);
        Collection<Film> films = filmDbStorage.getFilms();
        assertEquals(films.size(), 1);
        assertTrue(films.contains(film));
    }


    @Test
    void updateFilm() {
        Film film = createFilm1();
        film = filmDbStorage.createFilm(film);
        Film updateFilm = createFilm2();
        updateFilm.setId(film.getId());
        updateFilm = filmDbStorage.updateFilm(updateFilm);
        Film finalFilm = filmDbStorage.getFilmById(film.getId());
        assertEquals(updateFilm, finalFilm);


    }

    @Test
    void updateNotExistingFilm() {
        Film film = createFilm1();
        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmDbStorage.updateFilm(film));
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);

    }

    @Test
    void deleteFilm() {
        Film film1 = createFilm1();
        film1 = filmDbStorage.createFilm(film1);
        filmDbStorage.deleteFilm(film1.getId());
        Collection<Film> films = filmDbStorage.getFilms();
        Assertions.assertTrue(films.isEmpty());
    }

    @Test
    void loadGenres() {
        Set<Genre> filmGenres1 = Set.of(genres[0], genres[1]);
        Film film1 = createFilm1();
        film1.setGenres(filmGenres1);
        film1 = filmDbStorage.createFilm(film1);
        filmDbStorage.setFilmGenre(film1);
        Set<Genre> filmGenres2 = Set.of(genres[3], genres[4]);
        Film film2 = createFilm2();
        film2.setGenres(filmGenres2);
        film2 = filmDbStorage.createFilm(film2);
        filmDbStorage.setFilmGenre(film2);

        List<Film> films = List.of(film1, film2);
        films = filmDbStorage.loadFilmGenres(films);
        Set<Genre> gottenGenres1 = films.get(0).getGenres();

        assertEquals(gottenGenres1.size(), filmGenres1.size());
        assertTrue(gottenGenres1.containsAll(filmGenres1));
        assertTrue(filmGenres1.containsAll(gottenGenres1));

        Set<Genre> gottenGenres2 = films.get(1).getGenres();
        assertEquals(gottenGenres2.size(), filmGenres2.size());
        assertTrue(gottenGenres2.containsAll(filmGenres2));
        assertTrue(filmGenres2.containsAll(gottenGenres2));
    }

    @Test
    void loadEmptyGenres() {
        List<Film> films = Collections.emptyList();
        films = filmDbStorage.loadFilmGenres(films);
        assertTrue(films.isEmpty());
    }

    @Test
    void deleteGenres() {
        Set<Genre> filmGenres = Set.of(genres[0], genres[1]);
        Film film = createFilm1();
        film.setGenres(filmGenres);
        film = filmDbStorage.createFilm(film);
        filmDbStorage.setFilmGenre(film);
        filmDbStorage.deleteFilmGenre(film.getId());

        Film returningFilm = filmDbStorage.loadFilmGenre(film);
        Set<Genre> gettingGenres = returningFilm.getGenres();

        assertTrue(gettingGenres.isEmpty());
    }

    @Test
    void updateGenre() {
        Set<Genre> filmGenres = Set.of(genres[0], genres[1]);
        Film film = createFilm1();
        film.setGenres(filmGenres);
        film = filmDbStorage.createFilm(film);
        filmDbStorage.setFilmGenre(film);
        Set<Genre> filmGenres2 = Set.of(genres[3], genres[4]);
        film.setGenres(filmGenres2);
        filmDbStorage.updateFilmGenre(film);

        Film returningFilm = filmDbStorage.loadFilmGenre(film);
        Set<Genre> gottenGenres = returningFilm.getGenres();

        assertEquals(gottenGenres.size(), filmGenres2.size());
        assertTrue(gottenGenres.containsAll(filmGenres2));
        assertTrue(filmGenres2.containsAll(gottenGenres));
    }

}



