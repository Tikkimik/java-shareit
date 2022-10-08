package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto) throws CreatingException;

    UserDto updateUser(Long userId, UserDto userDto) throws CreatingException;

    List<UserDto> getAllUsers();

    UserDto getUser(Long userId);

    void deleteUser(Long userId);

}

