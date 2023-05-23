package ru.yandex.practicum.filmorate.test;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDbStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.test.TestCreate.genres;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreStorageTest {

    private final GenreDbStorage genreDbStorage;

    @Test
    void getAllTest() {
        List<Genre> allGenres = genreDbStorage.getAllGenres();
        assertEquals(allGenres.size(), 6);
        for (Genre genre : genres) {
            assertTrue(allGenres.contains(genre));
        }
    }

    @Test
    void getGenre() {
        Genre genre = genreDbStorage.getGenreById(1);
        assertEquals(genre, genres[0]);
    }

    @Test
    void getGenreByInvalidId() {
        ValidationException exception = assertThrows(ValidationException.class, () -> genreDbStorage.getGenreById(-1));
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
    }

}