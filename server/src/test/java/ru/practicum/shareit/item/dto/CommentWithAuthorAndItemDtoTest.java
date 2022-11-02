package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class CommentWithAuthorAndItemDtoTest {

    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    void commentDtoTest() throws Exception {
        CommentDto commentDto = new CommentDto(9L, "Привет от Васи! Отвертка топ.");

        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(9);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(commentDto.getText());
    }
}