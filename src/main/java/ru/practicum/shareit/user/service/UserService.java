package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto save(UserDto userDto) throws CreatingException;

    UserDto update(Long userId, UserDto userDto) throws NotFoundParameterException;

    List<UserDto> findAll();

    UserDto findById(Long userId) throws NotFoundParameterException;

    void deleteById(Long userId) throws NotFoundParameterException;

}

