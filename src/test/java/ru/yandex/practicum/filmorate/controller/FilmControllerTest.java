package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createFilm_ValidFilm_Returns200() throws Exception {
        Film film = Film.builder()
                .name("Valid Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andDo(result -> {
                    System.out.println("=== DEBUG ===");
                    System.out.println("Request URI: " + result.getRequest().getRequestURI());
                    System.out.println("Status: " + result.getResponse().getStatus());
                    System.out.println("Response: " + result.getResponse().getContentAsString());
                    System.out.println("Headers: " + result.getResponse().getHeaderNames());
                })
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void createFilm_EmptyName_Returns400() throws Exception {
        String filmJson = "{\"name\":\"\", \"description\":\"Description\", \"releaseDate\":\"2000-01-01\", \"duration\":120}";

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilm_Description201Chars_Returns400() throws Exception {
        String longDescription = "a".repeat(201);
        String filmJson = "{\"name\":\"Film\", \"description\":\"" + longDescription + "\", \"releaseDate\":\"2000-01-01\", \"duration\":120}";

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilm_ReleaseDateBefore1895_Returns400() throws Exception {
        String filmJson = "{\"name\":\"Film\", \"description\":\"Description\", \"releaseDate\":\"1895-12-27\", \"duration\":120}";

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilm_NegativeDuration_Returns400() throws Exception {
        String filmJson = "{\"name\":\"Film\", \"description\":\"Description\", \"releaseDate\":\"2000-01-01\", \"duration\":-10}";

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilm_EmptyBody_Returns400() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // Пустой JSON объект
                .andExpect(status().isBadRequest());
    }
}