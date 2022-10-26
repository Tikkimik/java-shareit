package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void toDtoTest() throws Exception {

        ItemDto itemDto = new ItemDto();
        itemDto.setId(7L);
        itemDto.setName("Pistol");
        itemDto.setDescription("James Bond pistol");
        itemDto.setAvailable(true);
        itemDto.setRequestId(55L);

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(7);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(itemDto.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(55);
    }
}