package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Simple integration test to verify the database setup works correctly.
 * This test verifies that reference data (genres and MPA ratings) is loaded properly.
 */
@JdbcTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({GenreDbStorage.class, MpaDbStorage.class})
public class DatabaseIntegrationTest {
    private final GenreDbStorage genreStorage;
    private final MpaDbStorage mpaStorage;

    @Test
    public void testDatabaseSetupWithReferenceData() {
        // Test that all expected genres are available
        List<Genre> genres = genreStorage.getAllGenres();
        assertThat(genres).hasSize(6);
        
        // Test that all expected MPA ratings are available
        List<MpaRating> mpaRatings = mpaStorage.getAllMpaRatings();
        assertThat(mpaRatings).hasSize(5);
        
        // Test specific genre
        Genre comedy = genreStorage.getGenre(1);
        assertThat(comedy.getName()).isEqualTo("Комедия");
        
        // Test specific MPA rating
        MpaRating g = mpaStorage.getMpaRating(1);
        assertThat(g.getName()).isEqualTo("G");
    }
}