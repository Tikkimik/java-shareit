package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static ru.practicum.shareit.item.dto.ItemWithBookingDto.BookingBookerDto.createBookingItemDto;

@JsonTest
class ItemWithBookingDtoTest {

    @Autowired
    private JacksonTester<ItemWithBookingDto> json;

    @Autowired
    private JacksonTester<ItemWithBookingDto.BookingBookerDto> json2;

    @Test
    void itemWithBookingDtoTestTest() throws Exception {
        ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto(
                7L,
                "Печь",
                "Печет булочки",
                true,
                new ItemWithBookingDto.BookingBookerDto(1L,1L),
                createBookingItemDto(new Booking(1L, LocalDateTime.now(), LocalDateTime.now(), new Item(1L, "a", "b", true, null, null), new User(1L, "Petya", "google@gmail.com"), null)),
                null);

        JsonContent<ItemWithBookingDto> result = json.write(itemWithBookingDto);

        assertThat(result).isEqualTo("{\"id\":7,\"name\":\"Печь\",\"description\":\"Печет булочки\",\"available\":true,\"nextBooking\":{\"id\":1,\"bookerId\":1},\"lastBooking\":{\"id\":1,\"bookerId\":1},\"comments\":null}");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(7);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemWithBookingDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemWithBookingDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathArrayValue("$.comments").isEqualTo(null);
    }
}