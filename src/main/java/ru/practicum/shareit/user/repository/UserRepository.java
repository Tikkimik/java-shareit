package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.exceptions.IncorrectParameterException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class UserRepository {
    private final Map<Long, User> userStorage;
    private Long id;

    public UserRepository() {
        this.userStorage = new HashMap<>();
        this.id = 1L;
    }

    private Long generateId() {
        return id++;
    }

    public User createUser(User user) throws CreatingException {
        if (checkUserEmail(user)) {
            Long id = generateId();
            user.setId(id);
            userStorage.put(id, user);
            return user;
        }
        return null;
    }

    public User getUser(Long userId) {
        return userStorage.get(userId);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userStorage.values());
    }

    public boolean checkUserEmail(User user) throws CreatingException {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(user.getEmail());

        if (!matcher.matches()) {
            throw new IncorrectParameterException("wrong email address");
        }

        for (User u : userStorage.values()) {
            if (u.getEmail().equals(user.getEmail())) {
                throw new CreatingException("email duplicate");
            }
        }
        return true;
    }

    public void deleteUser(Long userId) {
        userStorage.remove(userId);
    }
}
