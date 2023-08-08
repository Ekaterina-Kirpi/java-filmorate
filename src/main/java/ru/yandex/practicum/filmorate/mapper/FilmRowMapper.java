package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getInt("films.id");//int
        String name = rs.getString("films.name");
        String description = rs.getString("films.description");
        int duration = rs.getInt("films.duration");
        LocalDate releaseDate = rs.getDate("films.release_date").toLocalDate();
        Mpa mpa = new Mpa(rs.getInt("films.rating_id"), rs.getString("mpa_name"));
        Film film = new Film(name, description, releaseDate, duration);
        film.setId(id);
        film.setMpa(mpa);
        return film;
    }
}
