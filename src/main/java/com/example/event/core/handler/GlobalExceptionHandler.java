package com.example.event.core.handler;

import com.example.event.core.exception.*;
import com.example.event.dto.response.ErrorResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponseDto handleEventNotFound(EventNotFoundException exception) {
        LOGGER.warn("Event not found {}", exception.getMessage());
        ErrorResponseDto error = new ErrorResponseDto();
        error.setMessage("Event not exist");
        error.setThrowable(exception);
        error.setHttpStatus(HttpStatus.NOT_FOUND);
        return error;
    }

    @ExceptionHandler(CountryNotFoundExeception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponseDto handleCountryNotFound(CountryNotFoundExeception exception) {
        LOGGER.warn("Country not found {}", exception.getMessage());
        ErrorResponseDto error = new ErrorResponseDto();
        error.setMessage("Country not exist");
        error.setThrowable(exception);
        error.setHttpStatus(HttpStatus.NOT_FOUND);
        return error;
    }

    @ExceptionHandler(ParticipantAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponseDto handleParticipantAlreadyExist(ParticipantAlreadyExistException exception) {
        LOGGER.warn("Participant already exist {}", exception.getMessage());
        ErrorResponseDto error = new ErrorResponseDto();
        error.setMessage("Participant already exist");
        error.setThrowable(exception);
        error.setHttpStatus(HttpStatus.CONFLICT);
        return error;
    }


    @ExceptionHandler(ParticipantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponseDto handleParticipantNotFoundException(ParticipantNotFoundException exception) {
        LOGGER.warn("Participant not found {}", exception.getMessage());
        ErrorResponseDto error = new ErrorResponseDto();
        error.setMessage("Participant not found");
        error.setThrowable(exception);
        error.setHttpStatus(HttpStatus.NOT_FOUND);
        return error;
    }



    @ExceptionHandler(ObjectNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponseDto handleObjectNotValidException(ObjectNotValidException exception) {
        LOGGER.warn("Object not valid {}", exception.getMessage());
        ErrorResponseDto error = new ErrorResponseDto();
        error.setMessage(String.join("|", exception.getError()));
        error.setThrowable(exception);
        error.setHttpStatus(HttpStatus.BAD_REQUEST);
        return error;
    }

    @ExceptionHandler(ObjectAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponseDto handleObjectAlreadyExist(ObjectAlreadyExistException exception) {
        LOGGER.warn("Object already exist {}", exception.getMessage());
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage("Object already exist");
        errorResponseDto.setThrowable(exception);
        errorResponseDto.setHttpStatus(HttpStatus.CONFLICT);
        return errorResponseDto;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponseDto handleIlegalArgumentException(IllegalArgumentException exception) {
        LOGGER.warn("Invalid argument provided {}", exception.getMessage());
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage("Invalid argument provided");
        errorResponseDto.setThrowable(exception);
        errorResponseDto.setHttpStatus(HttpStatus.BAD_REQUEST);
        return errorResponseDto;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponseDto handleException(Exception exception) {
        LOGGER.error("Unexpected error", exception);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(exception.getMessage());
        errorResponseDto.setThrowable(exception);
        errorResponseDto.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return errorResponseDto;
    }



}
