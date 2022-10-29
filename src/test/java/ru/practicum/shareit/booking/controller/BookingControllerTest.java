package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithItemAndUserDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {

    @MockBean
    private BookingService bookingService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    private BookingDto bookingDto;
    private BookingWithItemAndUserDto bookingWithItemAndUserDto;

    @BeforeEach
    void beforeEach() {
        bookingDto = new BookingDto(
                1L,
                null,
                null,
                1L
        );

        bookingWithItemAndUserDto = new BookingWithItemAndUserDto(
                1L,
                null,
                null,
                null,
                null,
                BookingStatus.APPROVED
        );
    }

    @Test
    void getByIdTest() throws Exception {
        when(bookingService.getById(any(long.class), any(long.class)))
                .thenReturn(bookingWithItemAndUserDto);

        mvc.perform(MockMvcRequestBuilders.get("/bookings/9")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingWithItemAndUserDto.getId()), long.class))
                .andExpect(jsonPath("$.status", is(bookingWithItemAndUserDto.getStatus().toString())));
    }

    @Test
    void getBookingByBookerTest() throws Exception {
        List<BookingWithItemAndUserDto> list = new ArrayList<>();
        list.add(bookingWithItemAndUserDto);

        when(bookingService.getBookingByBooker(any(long.class), any(BookingStatus.class), any(Pageable.class)))
                .thenReturn(list);

        mvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(list.size())))
                .andExpect(jsonPath("$[0].id", is(bookingWithItemAndUserDto.getId()), long.class))
                .andExpect(jsonPath("$[0].status", is(bookingWithItemAndUserDto.getStatus().toString())));
    }

    @Test
    void getBookingByItemOwnerTest() throws Exception {
        List<BookingWithItemAndUserDto> list = new ArrayList<>();
        list.add(bookingWithItemAndUserDto);

        when(bookingService.getBookingByItemOwner(any(long.class), any(BookingStatus.class), any(Pageable.class)))
                .thenReturn(list);

        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(list.size())))
                .andExpect(jsonPath("$[0].id", is(bookingWithItemAndUserDto.getId()), long.class))
                .andExpect(jsonPath("$[0].status", is(bookingWithItemAndUserDto.getStatus().toString())));
    }

    @Test
    void addBookingTest() throws Exception {
        when(bookingService.addBooking(any(long.class), any(BookingDto.class)))
                .thenReturn(bookingWithItemAndUserDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingWithItemAndUserDto.getId()), long.class))
                .andExpect(jsonPath("$.status", is(bookingWithItemAndUserDto.getStatus().toString())));

    }

    @Test
    void approveTest() throws Exception {
        when(bookingService.approve(any(long.class), any(long.class), any(boolean.class)))
                .thenReturn(bookingWithItemAndUserDto);

        mvc.perform(MockMvcRequestBuilders.patch("/bookings/155?approved=true")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingWithItemAndUserDto.getId()), long.class))
                .andExpect(jsonPath("$.status", is(bookingWithItemAndUserDto.getStatus().toString())));
    }
}