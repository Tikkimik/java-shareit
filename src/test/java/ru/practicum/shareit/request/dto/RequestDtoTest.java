package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@JsonTest
class RequestDtoTest {

    @Autowired
    private JacksonTester<RequestDto> json;

    @Test
    void testItemRequestDto() throws Exception {
        RequestDto dto = new RequestDto(11L, "Pizza", LocalDateTime.now());

        JsonContent<RequestDto> test = json.write(dto);

        assertThat(test).extractingJsonPathNumberValue("$.id").isEqualTo(11);
        assertThat(test).extractingJsonPathStringValue("$.description").isEqualTo("Pizza");
    }
}