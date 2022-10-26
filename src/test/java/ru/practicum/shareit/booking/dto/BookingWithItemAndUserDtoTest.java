package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
@JsonTest
class BookingWithItemAndUserDtoTest {

        @Autowired
        private JacksonTester<BookingWithItemAndUserDto> json;

        Booking booking = new Booking(1L,
                null,
                null,
                new Item(1L, "Зажигалка", "Вроде рабочая нашёл во дворе.", true, new User(1L, "Петка", "vasyan@rambler.kz"), null),
                new User(2L, "Алёша", "02@mail.ru"),
                null);

    @Test
    void bookingWithItemAndUserDtoTest() throws Exception {
        BookingWithItemAndUserDto bookingWithItemAndUserDto =  new BookingWithItemAndUserDto(
                    booking.getId(),
                    booking.getStart(),
                    booking.getEnd(),
                    new BookingWithItemAndUserDto.ItemInfoDto(booking.getItem().getId(), booking.getItem().getName()),
                    new BookingWithItemAndUserDto.UserInfoDto(booking.getBooker().getId()),
                    booking.getStatus()
            );


        JsonContent<BookingWithItemAndUserDto> result = json.write(bookingWithItemAndUserDto);

        assertThat(result).isEqualTo("{\"id\":1,\"start\":null,\"end\":null,\"item\":{\"id\":1,\"name\":\"Зажигалка\"},\"booker\":{\"id\":2},\"status\":null}");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }

}