package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentWithAuthorAndItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private ItemDto itemDto;

    private ItemWithBookingDto itemWithBookingDto;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Отвертка");
        itemDto.setDescription("Болтик ей не закрутиш");
        itemDto.setAvailable(true);
        itemDto.setRequestId(3L);

        itemWithBookingDto = new ItemWithBookingDto(
                1L, "Отвертка", "Болтик ей не закрутиш",
                true, null, null, null);
    }
    @Test
    void createItemTest() throws Exception {
        when(itemService.createItem(any(long.class), any(ItemDto.class)))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    void updateItemTest() throws Exception {
        when(itemService.updateItem(any(long.class), any(long.class), any(ItemDto.class)))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/9")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    void getAllByUserIdTest() throws Exception {
        List<ItemWithBookingDto> list = new ArrayList<>();
        list.add(itemWithBookingDto);
        when(itemService.getAllByUserId(any(long.class)))
                .thenReturn(list);
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(list.size())))
                .andExpect(jsonPath("$[0].id", is(list.get(0).getId()), long.class))
                .andExpect(jsonPath("$[0].name", is(list.get(0).getName())))
                .andExpect(jsonPath("$[0].description", is(list.get(0).getDescription())))
                .andExpect(jsonPath("$[0].available", is(list.get(0).getAvailable())));
    }

    @Test
    void getItemTest() throws Exception {
        when(itemService.getItem(any(long.class), any(long.class)))
                .thenReturn(itemWithBookingDto);
        mvc.perform(get("/items/8805553535")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemWithBookingDto.getId()), long.class))
                .andExpect(jsonPath("$.name", is(itemWithBookingDto.getName())))
                .andExpect(jsonPath("$.description", is(itemWithBookingDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemWithBookingDto.getAvailable())));
    }

    @Test
    void searchAvailableItemTest() throws Exception {
        List<ItemDto> list = new ArrayList<>();
        list.add(itemDto);
        when(itemService.searchAvailableItem(any(String.class)))
                .thenReturn(list);
        mvc.perform(get("/items/search?text=sometext")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(list.size())))
                .andExpect(jsonPath("$[0].id", is(list.get(0).getId()), long.class))
                .andExpect(jsonPath("$[0].name", is(list.get(0).getName())))
                .andExpect(jsonPath("$[0].description", is(list.get(0).getDescription())))
                .andExpect(jsonPath("$[0].available", is(list.get(0).getAvailable())));
    }

    @Test
    void addCommentTest() throws Exception {
        CommentDto commentDto = new CommentDto(1L, "text");
        CommentWithAuthorAndItemDto commentWithAuthorAndItemDto =
                new CommentWithAuthorAndItemDto(1L, "Здарова! Отвертка супер!", "Васёк", null);

        when(itemService.addComment(any(long.class), any(long.class), any(CommentDto.class)))
                .thenReturn(commentWithAuthorAndItemDto);

        mvc.perform(post("/items/9/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentWithAuthorAndItemDto.getId()), long.class))
                .andExpect(jsonPath("$.text", is(commentWithAuthorAndItemDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentWithAuthorAndItemDto.getAuthorName())));
    }

    @Test
    void deleteTest() throws Exception {
        mvc.perform(delete("/items/88")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }
}