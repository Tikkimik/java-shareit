package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto) throws CreatingException;

    UserDto updateUser(Long userId, UserDto userDto) throws CreatingException, NotFoundParameterException;

    List<UserDto> getAllUsers();

    UserDto getUser(Long userId) throws NotFoundParameterException;

    void deleteUser(Long userId) throws NotFoundParameterException;

}

