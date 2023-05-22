package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.xml.validation.Validator;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmTest {

    private Film film;
    private Validator validator;
    private static FilmService filmService;

//    @BeforeEach
//    void beforeEach() {
//        filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
//        film = new Film(1, "Пятеро друзей", "Пятеро друзей приезжают в город Бризуль." +
//                " Здесь они хотят разыскать господина Огюста Куглова, который задолжал им 20 миллионов.",
//                LocalDate.of(1980, 3, 25), 126, null);
//    }


    @Test
    void shouldCreate() {
        filmService.createFilm(film);
        List<Film> filmList = filmService.getFilms();
        assertEquals(1, filmList.size(), "Количество фильмов не совпадает");
        assertEquals(film.getName(), filmList.get(0).getName(), "Название фильма неверно");
        assertEquals(film.getDescription(), filmList.get(0).getDescription(), "Некорректное описание фильма");
        assertEquals(film.getDuration(), filmList.get(0).getDuration(), "Некорректная продолжительность фильма");
        assertEquals(film.getReleaseDate(), filmList.get(0).getReleaseDate(), "Некорректная дата релиза");
    }


    @Test
    void shouldUpdate() {
        filmService.createFilm(film);
        List<Film> filmList = filmService.getFilms();
        film.setName("Новое название");
        film.setDescription("Новое описание");
        film.setReleaseDate(LocalDate.of(2022, 1, 1));
        Film updatedFilm = filmService.updateFilm(film);
        assertEquals(film.getId(), updatedFilm.getId());
        assertEquals(film.getName(), updatedFilm.getName());
        assertEquals(film.getDescription(), updatedFilm.getDescription());
        assertEquals(film.getReleaseDate(), updatedFilm.getReleaseDate());
        assertEquals(film.getDuration(), updatedFilm.getDuration());
    }

    @Test
    void shouldGetAllFilms() {
        filmService.createFilm(film);
        Film newFilm = new Film(2, "2", "Описание",
                LocalDate.of(2022, 1, 1), 150, null);
        filmService.createFilm(newFilm);
        List<Film> filmList = filmService.getFilms();
        assertEquals(2, filmList.size());
        assertTrue(filmList.contains(film));
        assertTrue(filmList.contains(newFilm));
    }



    @Test
    public void shouldValidateLongDescription() {
        filmService.createFilm(film);
        String s = "Какой-то очень интересный фильм, длинный, и описание так же но для проверки достаточно повторить это 2 раза".repeat(2);
        film.setDescription(s);
        assertThrows(ValidationException.class, () -> {
            filmService.createFilm(film);
        });
    }

    @Test
    public void shouldValidateNegativeDuration() {
        filmService.createFilm(film);
        film.setDuration(-1);

        assertThrows(ValidationException.class, () -> {
            filmService.createFilm(film);
        });
    }

    @Test
    public void shouldValidateInvalidReleaseDate() {
        filmService.createFilm(film);
        film.setReleaseDate(LocalDate.of(1700, 1, 1));

        assertThrows(ValidationException.class, () -> {
            filmService.createFilm(film);
        });
    }

}