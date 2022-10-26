package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.exceptions.IncorrectParameterException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.helpers.Create;
import ru.practicum.shareit.helpers.Update;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto save(@Validated({Create.class}) @RequestBody UserDto userDto) throws CreatingException {
        checkUserEmail(userDto);
        return userService.save(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId,
                          @Validated({Update.class}) @RequestBody UserDto userDto) throws NotFoundParameterException {
        if (userDto.getEmail() != null) checkUserEmail(userDto);
        return userService.update(userId, userDto);
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable Long userId) throws NotFoundParameterException {
        return userService.findById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable Long userId) throws NotFoundParameterException {
        userService.deleteById(userId);
    }

    private void checkUserEmail(UserDto userDto) throws IncorrectParameterException {

        if (userDto.getEmail() == null)
            throw new IncorrectParameterException("Exception: Email address cannot be null");

        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(userDto.getEmail());

        if (!matcher.matches())
            throw new IncorrectParameterException("Exception: email address not verified.");
    }
}
