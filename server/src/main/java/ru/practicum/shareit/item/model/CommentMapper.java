package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentWithAuthorAndItemDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public static Comment toComment(CommentDto commentDto, User user, Item item) {
        return new Comment(commentDto.getId(), commentDto.getText(), item, user, LocalDateTime.now());
    }

    public static CommentWithAuthorAndItemDto toCommentWithAuthorAndItemDto(Comment comment) {
        return new CommentWithAuthorAndItemDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static List<CommentWithAuthorAndItemDto> toCommentWithAuthorAndItemDto(List<Comment> comments) {
        return comments
                .stream()
                .map(CommentMapper::toCommentWithAuthorAndItemDto)
                .collect(Collectors.toList());
    }
}