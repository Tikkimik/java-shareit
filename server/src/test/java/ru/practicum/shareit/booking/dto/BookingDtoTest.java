package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void bookingWithItemAndUserDtoTest() throws Exception {
        BookingDto bookingDto = new BookingDto(
                77L,
                LocalDateTime.of(1999, 12, 31, 23, 59),
                LocalDateTime.of(2000, 1, 1, 12, 0),
                123123123L);

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(77);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(123123123);
        assertThat(result).isEqualTo("{\"id\":77,\"start\":\"1999-12-31T23:59:00\",\"end\":\"2000-01-01T12:00:00\",\"itemId\":123123123}");
    }
}
