package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({MpaDbStorage.class})
class MpaDbStorageTest {
    private final MpaDbStorage mpaStorage;

    @Test
    public void testGetMpaById() {
        MpaRating mpa = mpaStorage.getMpaRating(1);

        assertThat(mpa)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    public void testGetAllMpa() {
        List<MpaRating> mpaRatings = mpaStorage.getAllMpaRatings();

        assertThat(mpaRatings)
                .hasSize(5)
                .extracting(MpaRating::getName)
                .contains("G", "PG", "PG-13", "R", "NC-17");
    }

    @Test
    public void testGetMpaByIdNotFound() {
        try {
            mpaStorage.getMpaRating(999);
            assertThat(false).isTrue();
        } catch (Exception e) {
            assertThat(e).isNotNull();
        }
    }
}