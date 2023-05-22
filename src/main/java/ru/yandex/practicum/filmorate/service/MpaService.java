package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {

    @Autowired
    private final MpaStorage mpaStorage;

    public List<Mpa> getAll() {
        return mpaStorage.getAllRatingMpa();
    }

    public Mpa getMpaById(Integer id) {
        return mpaStorage.getMpaById(id);
    }

}