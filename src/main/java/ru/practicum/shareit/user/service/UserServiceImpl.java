package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.model.UserMapper.toUser;
import static ru.practicum.shareit.user.model.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto save(UserDto userDto) {
        return toUserDto(userRepository.save(toUser(userDto)));
    }

    @Override
    public UserDto findById(Long userId) throws NotFoundParameterException {
        return toUserDto(userRepository.findById(userId).orElseThrow(() ->
                new NotFoundParameterException("Exception: Wrong user id.")));
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) throws NotFoundParameterException {
        User updatedUser = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundParameterException("Exception: Wrong user id."));

        if (userDto.getName() != null) updatedUser.setName(userDto.getName());
        if (userDto.getEmail() != null) updatedUser.setEmail(userDto.getEmail());
        return toUserDto(userRepository.save(updatedUser));
    }
}
