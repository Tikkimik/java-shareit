package ru.practicum.shareit.booking;

import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class Booking {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private String item;
    private User booker;
    private String status;
}
