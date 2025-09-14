package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;

    @Override
    public Film addFilm(Film film) {
        film.setId(filmId++);
        films.put(film.getId(), film);
        log.info("Добавлен фильм с ID: {}", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм не найден");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilm(Integer id) {
        return films.get(id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        Film film = films.get(filmId);
        if (film != null) {
            if (film.getLikes() == null) {
                film.setLikes(new HashSet<>());
            }
            film.getLikes().add(userId);
            log.info("Добавлен лайк от пользователя {} к фильму {}", userId, filmId);
        }
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        Film film = films.get(filmId);
        if (film != null && film.getLikes() != null) {
            film.getLikes().remove(userId);
            log.info("Удален лайк пользователя {} с фильма {}", userId, filmId);
        }
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return films.values().stream()
                .sorted((film1, film2) -> {
                    int likes1 = film1.getLikes() != null ? film1.getLikes().size() : 0;
                    int likes2 = film2.getLikes() != null ? film2.getLikes().size() : 0;
                    return Integer.compare(likes2, likes1); // По убыванию
                })
                .limit(count)
                .collect(Collectors.toList());
    }
}