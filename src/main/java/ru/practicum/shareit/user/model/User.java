package ru.practicum.shareit.user.model;

import lombok.*;

import org.springframework.format.annotation.DateTimeFormat;


import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * TODO Sprint add-controllers.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Min(1)
    private Long id;
    @NotBlank
    private String name;
    private String email;
}
