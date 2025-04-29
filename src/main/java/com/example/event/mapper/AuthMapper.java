package com.example.event.mapper;


import com.example.event.dto.auth.RegisterRequest;
import com.example.event.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(source = "firstName", target ="firstName" )
    @Mapping(source = "lastName", target ="lastName" )
    @Mapping(source = "email", target ="email" )
    @Mapping(source = "role", target = "role")
    User fromRegister(RegisterRequest registerRequest);
}
