package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto save(@RequestBody UserDto userDto) throws CreatingException {

        log.info("Create new user.");
        return userService.save(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId,
                           @RequestBody UserDto userDto) throws NotFoundParameterException {

        log.info("Update user.");
        return userService.update(userId, userDto);
    }

    @GetMapping
    public List<UserDto> findAll() {
        log.info("Get all users.");
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable Long userId) throws NotFoundParameterException {
        log.info("Get user by user id.");
        return userService.findById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable Long userId) throws NotFoundParameterException {
        log.info("Delete user by user id.");
        userService.deleteById(userId);
    }
}