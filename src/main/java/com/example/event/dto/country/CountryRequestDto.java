package com.example.event.dto.country;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryRequestDto {

    @NotBlank(message = "The name field must be filled in")
    private String name;
}
