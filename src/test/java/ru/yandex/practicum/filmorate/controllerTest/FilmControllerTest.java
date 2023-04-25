package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import javax.xml.validation.Validator;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmControllerTest {


    //   @Autowired
    private FilmController filmController;
    private Film film;

    private Validator validator;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
        film = new Film(1, "Пятеро друзей", "Пятеро друзей приезжают в город Бризуль." +
                " Здесь они хотят разыскать господина Огюста Куглова, который задолжал им 20 миллионов.",
                LocalDate.of(1980, 03, 25), 126);
    }


    @Test
    void shouldCreate() {
        filmController.create(film);
        List<Film> filmList = filmController.getFilms();
        assertEquals(1, filmList.size(), "Количество фильмов не совпадает");
        assertEquals(film.getName(), filmList.get(0).getName(), "Название фильма неверно");
        assertEquals(film.getDescription(), filmList.get(0).getDescription(), "Некорректное описание фильма");
        assertEquals(film.getDuration(), filmList.get(0).getDuration(), "Некорректная продолжительность фильма");
        assertEquals(film.getReleaseDate(), filmList.get(0).getReleaseDate(), "Некорректная дата релиза");
    }


    @Test
    void shouldUpdate() {
        filmController.create(film);
        List<Film> filmList = filmController.getFilms();
        film.setName("Новое название");
        film.setDescription("Новое описание");
        film.setReleaseDate(LocalDate.of(2022, 1, 1));
        Film updatedFilm = filmController.update(film);
        assertEquals(film.getId(), updatedFilm.getId());
        assertEquals(film.getName(), updatedFilm.getName());
        assertEquals(film.getDescription(), updatedFilm.getDescription());
        assertEquals(film.getReleaseDate(), updatedFilm.getReleaseDate());
        assertEquals(film.getDuration(), updatedFilm.getDuration());
    }

    @Test
    void shouldGetAllFilms() {
        filmController.create(film);
        Film newFilm = new Film(2, "2", "Описание", LocalDate.of(2022, 1, 1), 150);
        filmController.create(newFilm);
        List<Film> filmList = filmController.getFilms();
        assertEquals(2, filmList.size());
        assertTrue(filmList.contains(film));
        assertTrue(filmList.contains(newFilm));
    }


    @Test
    public void shouldValidateEmptyName() {
        filmController.create(film);
        film.setName("");
        assertThrows(ValidationException.class, () -> {
            filmController.validateFilm(film);
        });
    }

    @Test
    public void shouldValidateLongDescription() {
        filmController.create(film);
        String s = "Какой-то очень интересный фильм, длинный, и описание так же но для проверки достаточно повторить это 2 раза".repeat(2);
        film.setDescription(s);
        assertThrows(ValidationException.class, () -> {
            filmController.validateFilm(film);
        });
    }

    @Test
    public void shouldValidateNegativeDuration() {
        filmController.create(film);
        film.setDuration(-1);

        assertThrows(ValidationException.class, () -> {
            filmController.validateFilm(film);
        });
    }

    @Test
    public void shouldValidateInvalidReleaseDate() {
        filmController.create(film);
        film.setReleaseDate(LocalDate.of(1700, 1, 1));

        assertThrows(ValidationException.class, () -> {
            filmController.validateFilm(film);
        });
    }

}