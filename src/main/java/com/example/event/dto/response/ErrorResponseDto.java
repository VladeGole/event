package com.example.event.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponseDto {

    private String message;

    private Throwable throwable;

    private HttpStatus httpStatus;
}
