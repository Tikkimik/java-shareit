package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemWithBookingDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private Long request;
    private BookingDto nextBooking;
    private BookingDto lastBooking;
    private List<CommentWithAuthorAndItemDto> comments;

    public ItemWithBookingDto(Long id, String name, String description, Boolean available, Long owner, Long request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
    }

}