package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.IncorrectParameterException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.model.UserMapper.toUser;
import static ru.practicum.shareit.user.model.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {

        checkUserEmail(userDto);

        return toUserDto(userRepository.save(toUser(userDto)));
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) throws NotFoundParameterException {
        UserDto userFromStorage = getUser(userId);
        userFromStorage.setId(userId);
        if (userDto.getEmail() != null) {
            checkUserEmail(userDto);
            userFromStorage.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null)
            userFromStorage.setName(userDto.getName());
        userRepository.save(toUser(userFromStorage));
        return userFromStorage;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream().map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(Long userId) throws NotFoundParameterException {
        checkUserId(userId);
        return toUserDto(userRepository.getReferenceById(userId));
    }

    @Override
    public void deleteUser(Long userId) throws NotFoundParameterException {
        checkUserId(userId);
        userRepository.deleteAllById(List.of(userId));
    }

    private void checkUserId(Long userId) throws NotFoundParameterException {

        if (!userRepository.existsById(userId))
            throw new NotFoundParameterException("Exception: Wrong user id.");

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
