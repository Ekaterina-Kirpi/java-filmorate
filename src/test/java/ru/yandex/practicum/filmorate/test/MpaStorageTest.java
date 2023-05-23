package ru.yandex.practicum.filmorate.test;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.MpaDbStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.test.TestCreate.ratings;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaStorageTest {

    private final MpaDbStorage mpaDbStorage;

    @Test
    void getAllTest() {
        List<Mpa> allGenres = mpaDbStorage.getAllRatingMpa();
        assertEquals(allGenres.size(), 5);
        for (Mpa mpa : allGenres) {
            assertTrue(allGenres.contains(mpa));
        }
    }

    @Test
    void getMpa() {
        Mpa mpa = mpaDbStorage.getMpaById(1);
        assertEquals(mpa, ratings[0]);
    }

    @Test
    void getMpaByInvalidId() {
        ValidationException exception = assertThrows(ValidationException.class, () -> mpaDbStorage.getMpaById(-1));
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
    }

}