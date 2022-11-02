package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.request.dto.ItemRequestDescriptionDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestService itemRequestService;

    private static final String BIG_HEAD = "X-Sharer-User-Id";

    @Test
    void addTest() throws Exception {
        ItemRequestDescriptionDto itemRequestDto = new ItemRequestDescriptionDto("test");
        RequestDto requestDto = new RequestDto(1L, "test", LocalDateTime.now());

        Mockito.when(itemRequestService.add(Mockito.any(Long.class), Mockito.any(ItemRequestDescriptionDto.class)))
                .thenReturn(requestDto);

        mockMvc.perform(post("/requests")
                        .header(BIG_HEAD, 1L)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is(requestDto.getDescription()), String.class));


        Mockito.verify(itemRequestService, Mockito.times(1))
                .add(Mockito.any(Long.class), Mockito.any(ItemRequestDescriptionDto.class));
    }

    @Test
    void getByUserTest() throws Exception {
        ItemRequestDto request = new ItemRequestDto(1L, "test", LocalDateTime.now(), new ArrayList<>());
        List<ItemRequestDto> requests = List.of(request);

        Mockito.when(itemRequestService.getByUser(Mockito.any(Long.class)))
                .thenReturn(requests);

        mockMvc.perform(get("/requests")
                        .header(BIG_HEAD, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id", Matchers.is(request.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].description",
                        Matchers.is(request.getDescription()),
                        String.class));

        Mockito.verify(itemRequestService, Mockito.times(1))
                .getByUser(Mockito.any(Long.class));
    }

    @Test
    void getByIdTest() throws Exception {
        ItemRequestDto requestInfoDto = new ItemRequestDto(1L, "test", LocalDateTime.now(), new ArrayList<>());

        Mockito.when(itemRequestService.getById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(requestInfoDto);

        mockMvc.perform(get("/requests/1")
                        .header(BIG_HEAD, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(requestInfoDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description",
                        Matchers.is(requestInfoDto.getDescription()),
                        String.class));

        Mockito.verify(itemRequestService, Mockito.times(1))
                .getById(Mockito.anyLong(), Mockito.anyLong());
    }
}