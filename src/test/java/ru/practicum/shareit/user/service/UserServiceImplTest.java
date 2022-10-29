package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static ru.practicum.shareit.user.model.UserMapper.toUser;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;
    private MockitoSession mockitoSession;
    private User user;
    private UserDto userDto;

    @BeforeEach
    void beforeEach() {
        mockitoSession = Mockito.mockitoSession().initMocks(this).startMocking();
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
        user = new User(1L, "Andrey", "googlbubu@gmail.ru");
        userDto = new UserDto(1L, "Andrey", "googlbubu@gmail.ru");
    }

    @AfterEach
    void afterEach() {
        mockitoSession.finishMocking();
    }

    @Test
    void saveTest() throws CreatingException {
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        UserDto testUserDto = userService.save(userDto);

        Assertions.assertNotNull(testUserDto);
        Assertions.assertEquals(user.getId(), testUserDto.getId());
        Assertions.assertEquals(user.getName(), testUserDto.getName());
        Assertions.assertEquals(user.getEmail(), testUserDto.getEmail());

        Mockito.verify(userRepository, Mockito.times(1))
                .save(toUser(userDto));
    }

    @Test
    void updateUserTest() throws NotFoundParameterException {
        User hiImBoris = new User(69L, "nowImBoris", "Boris@mail.kz");

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(hiImBoris);

        UserDto testUserDto = userService.update(user.getId(), userDto);

        Assertions.assertNotNull(testUserDto);
        Assertions.assertEquals(hiImBoris.getId(), testUserDto.getId());
        Assertions.assertEquals(hiImBoris.getName(), testUserDto.getName());
        Assertions.assertEquals(hiImBoris.getEmail(), testUserDto.getEmail());
    }

    @Test
    void findByIdTest() throws NotFoundParameterException {
        User user = new User(1L, "test", "test@gmail.com");

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        UserDto foundUser = userService.findById(user.getId());

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(user.getId(), foundUser.getId());
        Assertions.assertEquals(user.getName(), foundUser.getName());
        Assertions.assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    void deleteTest() throws NotFoundParameterException {
        userService.deleteById(userDto.getId());

        Mockito.verify(userRepository, Mockito.times(1)).deleteById(userDto.getId());
    }

    @Test
    void findAllTest() {
        List<User> users = List.of(toUser(userDto));

        Mockito.doReturn(users).when(userRepository).findAll();

        List<UserDto> list = userService.findAll();

        MatcherAssert.assertThat(list.get(0).getId(), equalTo(userDto.getId()));
        MatcherAssert.assertThat(list.get(0).getEmail(), equalTo(userDto.getEmail()));
        MatcherAssert.assertThat(list.get(0).getName(), equalTo(userDto.getName()));
        MatcherAssert.assertThat(list.size(), equalTo(1));
    }
}