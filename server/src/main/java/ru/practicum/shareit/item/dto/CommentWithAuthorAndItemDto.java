package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentWithAuthorAndItemDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}