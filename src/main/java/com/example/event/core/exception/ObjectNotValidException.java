package com.example.event.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public class ObjectNotValidException extends RuntimeException {

   private final Set<String> error;
}
