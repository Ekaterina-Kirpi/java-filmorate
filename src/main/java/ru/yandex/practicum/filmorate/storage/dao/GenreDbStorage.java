package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.util.List;

@Slf4j
@Component
@Primary
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    private final GenreRowMapper genreRowMapper;

    @Autowired

    public GenreDbStorage(JdbcTemplate jdbcTemplate, GenreRowMapper genreRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreRowMapper = genreRowMapper;
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlAllFilms = "SELECT * FROM genre ORDER BY id;";
        return jdbcTemplate.query(sqlAllFilms, genreRowMapper);
    }

    @Override
    public Genre getGenreById(int id) {
        String sql = "SELECT * " +
                "FROM genre " +
                "WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, genreRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Рейтинг не найден");
        }
    }
}