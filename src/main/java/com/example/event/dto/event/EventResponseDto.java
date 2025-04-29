package com.example.event.dto.event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class EventResponseDto {

    private Long id;

    @NotBlank(message = "The name field must be filled in")
    private String name;

    @Positive(message = "Price must be positive")
    @NotNull(message = "The price field not must be null ")
    @Min(value = 50, message = "Price must be at least 50")
    private Integer price;

    @NotBlank(message = "The currency field must be filled in")
    private String currency;

    @NotNull(message = "Date is required")
    private LocalDate startDate;

    @NotNull(message = "Date is required")
    private LocalDate endDate;

    private String country;

    private String owner;

}
