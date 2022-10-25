package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemInfoDto> items;

    @lombok.Getter
    @lombok.Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemInfoDto {

        private Long id;
        private String name;
        private String description;
        private Boolean available;
        private Long requestId;
    }

}
