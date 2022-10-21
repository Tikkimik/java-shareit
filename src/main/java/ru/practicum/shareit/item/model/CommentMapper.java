package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentWithAuthorAndItemDto;

@Component
public class CommentMapper {
    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getText(),
                comment.getItem(),
                comment.getAuthor(),
                comment.getCreated()
        );
    }

    public Comment toComment(CommentDto commentDto) {
        return new Comment(commentDto.getId(),
                commentDto.getText(),
                commentDto.getItemId(),
                commentDto.getAuthorId(),
                commentDto.getCreated());
    }

    public CommentWithAuthorAndItemDto toCommentWithAuthorAndItemDto(Comment comment) {
        return new CommentWithAuthorAndItemDto(comment.getId(),
                comment.getText(),
                comment.getItem(),
                comment.getCreated());
    }

}