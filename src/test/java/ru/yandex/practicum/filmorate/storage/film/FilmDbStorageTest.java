package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, GenreDbStorage.class, MpaDbStorage.class})
class FilmDbStorageTest {
    private final FilmDbStorage filmStorage;

    @Test
    public void testAddFilm() {
        Film testFilm = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .mpa(MpaRating.builder().id(1).name("G").build())
                .genres(Set.of(Genre.builder().id(1).name("Комедия").build()))
                .build();

        Film addedFilm = filmStorage.addFilm(testFilm);

        assertThat(addedFilm).isNotNull();
        assertThat(addedFilm.getId()).isNotNull();
        assertThat(addedFilm.getName()).isEqualTo(testFilm.getName());
        assertThat(addedFilm.getDescription()).isEqualTo(testFilm.getDescription());
        assertThat(addedFilm.getReleaseDate()).isEqualTo(testFilm.getReleaseDate());
        assertThat(addedFilm.getDuration()).isEqualTo(testFilm.getDuration());
    }

    @Test
    public void testFindFilmById() {
        Film testFilm = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .mpa(MpaRating.builder().id(1).name("G").build())
                .genres(Set.of(Genre.builder().id(1).name("Комедия").build()))
                .build();

        Film addedFilm = filmStorage.addFilm(testFilm);
        Film foundFilm = filmStorage.getFilm(addedFilm.getId());

        assertThat(foundFilm)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", addedFilm.getId())
                .hasFieldOrPropertyWithValue("name", "Test Film");
    }

    @Test
    public void testGetAllFilms() {
        Film testFilm1 = Film.builder()
                .name("Test Film 1")
                .description("Test Description 1")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .mpa(MpaRating.builder().id(1).name("G").build())
                .genres(Set.of(Genre.builder().id(1).name("Комедия").build()))
                .build();

        Film testFilm2 = Film.builder()
                .name("Test Film 2")
                .description("Test Description 2")
                .releaseDate(LocalDate.of(2001, 1, 1))
                .duration(90)
                .mpa(MpaRating.builder().id(2).name("PG").build())
                .genres(Set.of(Genre.builder().id(2).name("Драма").build()))
                .build();

        filmStorage.addFilm(testFilm1);
        filmStorage.addFilm(testFilm2);

        List<Film> films = (List<Film>) filmStorage.getAllFilms();

        assertThat(films).hasSize(2);
    }


}