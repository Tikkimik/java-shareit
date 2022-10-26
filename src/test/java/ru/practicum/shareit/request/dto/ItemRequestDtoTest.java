package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void testItemRequestDto() throws Exception {
        ItemRequestDto dto = new ItemRequestDto(11L, "Pizza", LocalDateTime.now(), null);

        JsonContent<ItemRequestDto> test = json.write(dto);

        assertThat(test).extractingJsonPathNumberValue("$.id").isEqualTo(11);
        assertThat(test).extractingJsonPathStringValue("$.description").isEqualTo("Pizza");
    }
}