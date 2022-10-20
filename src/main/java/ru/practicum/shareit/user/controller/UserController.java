package ru.practicum.shareit.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.helperСlasses.Create;
import ru.practicum.shareit.helperСlasses.Update;
import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping
    public UserDto createUser(@Validated({Create.class}) @RequestBody UserDto userDto) throws CreatingException {
        return userService.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId,
                          @Validated({Update.class}) @RequestBody UserDto userDto) throws CreatingException, NotFoundParameterException {
        return userService.updateUser(userId, userDto);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) throws NotFoundParameterException {
        return userService.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

}
