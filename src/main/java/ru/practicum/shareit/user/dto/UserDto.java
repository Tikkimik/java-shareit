package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.helperСlasses.Create;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(groups = {Create.class})
    private String name;

    @NotBlank(groups = {Create.class})
    private String email;

}
