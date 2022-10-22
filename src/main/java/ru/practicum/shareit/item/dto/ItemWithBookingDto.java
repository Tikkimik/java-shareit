package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemWithBookingDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private Long request;
    private BookingDto nextBooking;
    private BookingDto lastBooking;
    private List<CommentWithAuthorAndItemDto> comments;

    public ItemWithBookingDto(Long id, String name, String description, Boolean available, User owner, Long request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
    }

}