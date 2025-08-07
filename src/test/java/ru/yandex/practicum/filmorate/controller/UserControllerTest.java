package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_ValidUser_Returns200() throws Exception {
        User user = User.builder()
                .email("email@test.ru")
                .login("login")
                .name("Name")
                .birthday(LocalDate.of(2000, 1, 1))
                .friends(new HashSet<>())
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    void createUser_InvalidEmail_Returns400() throws Exception {
        String userJson = "{\"email\":\"invalid-email\", \"login\":\"login\", \"name\":\"Name\", \"birthday\":\"2000-01-01\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }


    @Test
    void createUser_LoginWithSpaces_Returns400() throws Exception {
        String userJson = "{\"email\":\"email@test.ru\", \"login\":\"log in\", \"birthday\":\"2000-01-01\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_FutureBirthday_Returns400() throws Exception {
        String userJson = "{\"email\":\"email@test.ru\", \"login\":\"login\", \"name\":\"Name\", \"birthday\":\"" +
                LocalDate.now().plusDays(1) + "\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_EmptyName_UsesLoginAsName() throws Exception {
        String userJson = "{\"email\":\"email@test.ru\", \"login\":\"login\", \"name\":\"\", \"birthday\":\"2000-01-01\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("login"));
    }

    @Test
    void createUser_EmptyBody_Returns400() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}