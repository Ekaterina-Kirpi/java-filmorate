package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;

import java.util.List;

@Slf4j
@Component
@Primary
public class MpaDbStorage implements MpaStorage {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MpaRowMapper mpaRowMapper;

    @Override
    public List<Mpa> getAllRatingMpa() {
        String sql = "SELECT * " +
                "FROM rating_mpa";
        return jdbcTemplate.query(sql, mpaRowMapper);
    }

    @Override
    public Mpa getMpaById(int id) {
        String sql = "SELECT * " +
                "FROM rating_mpa " +
                "WHERE rating_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, mpaRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Рейтинг не найден");
        }
    }
}