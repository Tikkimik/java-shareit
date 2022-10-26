package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemRequestDescriptionDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDescriptionDto> json;

    @Test
    void testItemRequestDto() throws Exception {
        ItemRequestDescriptionDto dto = new ItemRequestDescriptionDto("Pizza");

        JsonContent<ItemRequestDescriptionDto> test = json.write(dto);

        assertThat(test).extractingJsonPathStringValue("$.description").isEqualTo("Pizza");
    }
}