package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class})
class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    public void testAddUser() {
        User testUser = User.builder()
                .email("test@example.com")
                .login("testLogin")
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        User addedUser = userStorage.addUser(testUser);

        assertThat(addedUser).isNotNull();
        assertThat(addedUser.getId()).isNotNull();
        assertThat(addedUser.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(addedUser.getLogin()).isEqualTo(testUser.getLogin());
        assertThat(addedUser.getName()).isEqualTo(testUser.getName());
        assertThat(addedUser.getBirthday()).isEqualTo(testUser.getBirthday());
    }

    @Test
    public void testFindUserById() {
        User testUser = User.builder()
                .email("test@example.com")
                .login("testLogin")
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        User addedUser = userStorage.addUser(testUser);
        User foundUser = userStorage.getUser(addedUser.getId());

        assertThat(foundUser)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", addedUser.getId())
                .hasFieldOrPropertyWithValue("email", "test@example.com");
    }

    @Test
    public void testGetAllUsers() {
        User testUser1 = User.builder()
                .email("test1@example.com")
                .login("testLogin1")
                .name("Test User 1")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        User testUser2 = User.builder()
                .email("test2@example.com")
                .login("testLogin2")
                .name("Test User 2")
                .birthday(LocalDate.of(1991, 2, 2))
                .build();

        userStorage.addUser(testUser1);
        userStorage.addUser(testUser2);

        List<User> users = (List<User>) userStorage.getAllUsers();

        assertThat(users).hasSize(2);
    }

    @Test
    public void testUpdateUser() {
        User testUser = User.builder()
                .email("test@example.com")
                .login("testLogin")
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        User addedUser = userStorage.addUser(testUser);

        User updatedUser = User.builder()
                .id(addedUser.getId())
                .email("updated@example.com")
                .login("updatedLogin")
                .name("Updated User")
                .birthday(LocalDate.of(1995, 5, 5))
                .build();

        User result = userStorage.updateUser(updatedUser);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(addedUser.getId());
        assertThat(result.getEmail()).isEqualTo("updated@example.com");
        assertThat(result.getLogin()).isEqualTo("updatedLogin");
        assertThat(result.getName()).isEqualTo("Updated User");
        assertThat(result.getBirthday()).isEqualTo(LocalDate.of(1995, 5, 5));
    }

    @Test
    public void testAddAndGetFriends() {
        User user1 = User.builder()
                .email("user1@example.com")
                .login("user1Login")
                .name("User 1")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        User user2 = User.builder()
                .email("user2@example.com")
                .login("user2Login")
                .name("User 2")
                .birthday(LocalDate.of(1991, 2, 2))
                .build();

        User addedUser1 = userStorage.addUser(user1);
        User addedUser2 = userStorage.addUser(user2);

        userStorage.addFriend(addedUser1.getId(), addedUser2.getId());

        List<User> friends = userStorage.getFriends(addedUser1.getId());

        assertThat(friends).hasSize(1);
        assertThat(friends.get(0).getId()).isEqualTo(addedUser2.getId());
    }
}