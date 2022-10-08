package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) throws CreatingException {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.createUser(user));
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) throws CreatingException {
        User userFromStorage = userRepository.getUser(userId);
        User user = UserMapper.toUser(userDto);

        if (user.getName() != null) {
            userFromStorage.setName(user.getName());
        }

        if (user.getEmail() != null) {
            if (userRepository.checkUserEmail(user)) {
                userFromStorage.setEmail(user.getEmail());
            }
        }

        return UserMapper.toUserDto(userFromStorage);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return UserMapper.toListUserDto(userRepository.getAllUsers());
    }

    @Override
    public UserDto getUser(Long userId) {
        return UserMapper.toUserDto(userRepository.getUser(userId));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }
}
