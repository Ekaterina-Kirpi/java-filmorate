package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikeService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")

@Slf4j

public class FilmController {
    private final FilmService filmService;


    private final LikeService likeService;

    @Autowired
    public FilmController(FilmService filmService, LikeService likeService) {
        this.filmService = filmService;
        this.likeService = likeService;
    }


    @GetMapping
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }


    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable long id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        likeService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        likeService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(value = "count", defaultValue = "10") int count) {
        return likeService.getTopLikedFilms(count);
    }
}