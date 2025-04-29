package com.example.event.mapper;

import com.example.event.dto.auth.RegisterRequest;
import com.example.event.model.Role;
import com.example.event.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class AuthMapperTest {

    private static AuthMapper authMapper;

    @BeforeAll
    static void beforeAll() {
        authMapper = Mappers.getMapper(AuthMapper.class);
    }

    @Test
    public void shouldMapRegisterToUser() {

        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Milan");
        request.setLastName("Marković");
        request.setEmail("milan@example.com");
        request.setRole(Role.ADMIN);

        User user = authMapper.fromRegister(request);

        assertEquals(request.getFirstName(), user.getFirstName());
        assertEquals(request.getLastName(), user.getLastName());
        assertEquals(request.getEmail(), user.getEmail());
        assertEquals(request.getRole(), user.getRole());

    }
}