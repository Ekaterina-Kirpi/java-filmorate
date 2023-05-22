package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Component
@Primary
public class FilmDbStorage implements FilmStorage {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private GenreStorage genreStorage;
    @Autowired
    private GenreRowMapper genreRowMapper;

    @Autowired
    private FilmRowMapper filmRowMapper;

    private final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);
    @Override
    public Film createFilm(Film film) {
        validateFilm(film);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("ID");
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("duration", film.getDuration());
        values.put("release_date", film.getReleaseDate());
        values.put("rating_id", film.getMpa().getId());

        Number userKey = simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(values));
        film.setId(userKey.longValue());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validateFilm(film);
        String sql = "UPDATE FILMS SET NAME = :name, DESCRIPTION = :description, DURATION = :duration, RELEASE_DATE = :release_date, " +
                "Rating_id = :rating_id WHERE ID = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("duration", film.getDuration())
                .addValue("release_date", film.getReleaseDate())
                .addValue("rating_id", film.getMpa().getId())
                .addValue("id", film.getId());
        int count = namedParameterJdbcTemplate.update(sql, sqlParameterSource);
        if (count == 0) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Фильма с id: " + film.getId() + " не существует");

        }
        return film;
    }

    @Override
    public List<Film> getFilms() {
        String sqlAllFilms = "SELECT f.*, m.name as mpa_name " +
                "FROM FILMS AS f JOIN rating_mpa AS m ON f.rating_id = m.rating_id";
        return namedParameterJdbcTemplate.query(sqlAllFilms, filmRowMapper);
    }

    @Override
    public Film getFilmById(long id) {
        String sqlFilmById = "SELECT *, rm.name as mpa_name FROM FILMS f, rating_mpa rm WHERE rm.Rating_id = f.Rating_id AND ID = ? ";
        List<Film> films = jdbcTemplate.query(sqlFilmById, filmRowMapper, id);
        if (films.isEmpty()) {
            throw new ValidationException(HttpStatus.NOT_FOUND, String.format("Фильм с id = %s не найден", id));
        }
        return films.get(0);
    }

    public void deleteFilmGenre(long id) {
        String sql = "DELETE FROM film_genre " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateFilmGenre(Film film) {
        deleteFilmGenre(film.getId());
        setFilmGenre(film);
    }

    public void setFilmGenre(Film film) {
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }
        String sql = "INSERT INTO film_genre (film_id, genre_id) " +
                "VALUES(?,?)";

        List<Genre> genres = new ArrayList<>(film.getGenres());
        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, film.getId());
                        ps.setInt(2, genres.get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return film.getGenres().size();
                    }
                });
    }

    public Film loadFilmGenre(Film film) {
        String sql = "SELECT film_genre.genre_id, genre.* " +
                "FROM film_genre " +
                "JOIN genre ON genre.id = film_genre.genre_id " +
                "WHERE film_genre.film_id IN  ( ? );";
        List<Genre> genres = jdbcTemplate.query(sql, genreRowMapper, film.getId());
        film.setGenres(new HashSet<>(genres));
        return film;
    }

    public List<Film> loadFilmGenres(List<Film> films) {
        if (films.isEmpty()) {
            return Collections.emptyList();
        }
        String idsStr = films.stream()
                .map(Film::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        String sql = "SELECT film_genre.*, genre.* " +
                "FROM film_genre " +
                "JOIN genre ON genre.id = film_genre.genre_id " +
                "WHERE film_id IN  (" + idsStr + ") Order by genre.id;";

        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
        Map<Long, Set<Genre>> genresByFilmId = mapList.stream()
                .collect(Collectors.groupingBy(k -> Long.parseLong(((Integer) k.get("film_id")).toString()),
                        mapping(k -> new Genre((Integer) k.get("genre_id"), (String) k.get("name")), toSet())));

        for (Film film : films) {
            Set<Genre> filmGenres = genresByFilmId.get(film.getId());
            if (filmGenres != null) {
                film.setGenres(filmGenres);
            }
        }
        return films;
    }


    @ResponseBody
    public void validateFilm(Film film) {
        int maxDescriptionLength = 200;
        if (film.getName() == null || film.getDescription() == null) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Некорректно введены данные фильма");

        } else if (film.getReleaseDate().isBefore(minReleaseDate)) {
            log.warn("Неверно указана дата выпуска");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Неверно указана дата выпуска");
        } else if (film.getDescription().length() > maxDescriptionLength) {
            log.warn("Максимальная длина описания - 200 символов");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Максимальная длина описания - 200 символов");
        } else if (film.getDuration() < 0) {
            log.warn("Продолжительность фильма должна быть положительной");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Продолжительность фильма должна быть положительной");
        }

    }

}

