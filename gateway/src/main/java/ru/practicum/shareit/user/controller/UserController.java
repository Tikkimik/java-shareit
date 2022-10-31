package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.IncorrectParameterException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.helpers.Create;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public  ResponseEntity<Object> save(@Validated({Create.class}) @RequestBody UserDto userDto) {
        checkUserEmail(userDto);
        log.info("Gateway: Create new user.");
        return userClient.save(userDto);
    }

    @PatchMapping("/{userId}")
    public  ResponseEntity<Object> update(@PathVariable Long userId,
                          @RequestBody UserDto userDto) throws NotFoundParameterException {
        if (userDto.getEmail() != null) checkUserEmail(userDto);
        log.info("Gateway: Update user.");
        return userClient.update(userId, userDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        log.info("Gateway: Get all users.");
        return userClient.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findById(@PathVariable Long userId) throws NotFoundParameterException {
        log.info("Gateway: Get user by user id.");
        return userClient.findById(userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteById(@PathVariable Long userId) throws NotFoundParameterException {
        log.info("Gateway: Delete user by user id.");
        return userClient.deleteById(userId);
    }

    private void checkUserEmail(UserDto userDto) throws IncorrectParameterException {

        if (userDto.getEmail() == null)
            throw new IncorrectParameterException("Exception: Email address cannot be null");

        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(userDto.getEmail());

        if (!matcher.matches())
            throw new IncorrectParameterException("Gateway: Exception: email address not verified.");
    }
}
