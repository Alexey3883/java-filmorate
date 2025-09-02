package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

public interface MpaStorage {
    MpaRating getMpaRating(Integer id);
    List<MpaRating> getAllMpaRatings();
}