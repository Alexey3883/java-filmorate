package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        User existingUser = userStorage.getUser(user.getId());
        if (existingUser == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        return userStorage.updateUser(user);
    }

    public User getUser(Integer id) {
        User user = userStorage.getUser(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
        return user;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(Integer userId, Integer friendId) {
        if (userId.equals(friendId)) {
            throw new ValidationException("Нельзя добавить себя в друзья");
        }

        User user = getUser(userId);
        User friend = getUser(friendId);

        ((ru.yandex.practicum.filmorate.storage.user.UserDbStorage) userStorage).addFriend(userId, friendId);
    }

    public void confirmFriend(Integer userId, Integer friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);

        ((ru.yandex.practicum.filmorate.storage.user.UserDbStorage) userStorage).confirmFriend(userId, friendId);
    }

    public void removeFriend(Integer userId, Integer friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);

        ((ru.yandex.practicum.filmorate.storage.user.UserDbStorage) userStorage).removeFriend(userId, friendId);
    }

    public List<User> getFriends(Integer userId) {
        User user = getUser(userId);

        return ((ru.yandex.practicum.filmorate.storage.user.UserDbStorage) userStorage).getFriends(userId);
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        User user = getUser(userId);
        User otherUser = getUser(otherId);

        return ((ru.yandex.practicum.filmorate.storage.user.UserDbStorage) userStorage)
                .getCommonFriends(userId, otherId);
    }
}