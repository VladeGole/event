package com.example.event.dto.participant;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantResponseDto {

    private Long id;

    @Email
    private String email;
}
