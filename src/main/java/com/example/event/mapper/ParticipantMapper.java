package com.example.event.mapper;

import com.example.event.dto.participant.ParticipantRequestDto;
import com.example.event.dto.participant.ParticipantResponseDto;
import com.example.event.model.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ParticipantMapper {


    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    Participant fromRequestDto(ParticipantRequestDto dto);

    @Mapping(source= "id", target = "id")
    @Mapping(source= "email", target = "email")
    ParticipantResponseDto toDto(Participant participant);

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    Participant updateFromRequest(ParticipantRequestDto dto, @MappingTarget Participant participant);
}
