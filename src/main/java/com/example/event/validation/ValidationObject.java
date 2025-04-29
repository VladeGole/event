package com.example.event.validation;

import com.example.event.core.exception.ObjectNotValidException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ValidationObject<T> {

    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    Validator validator = validatorFactory.getValidator();

    public void validate(T objectValidate) {
        Set<ConstraintViolation<T>> violations = validator.validate(objectValidate);
        if(!violations.isEmpty()){
            throw new ObjectNotValidException(violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet()));
        }
    }
}
