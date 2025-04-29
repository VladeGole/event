package com.example.event.service.participant;

import com.example.event.dto.participant.ParticipantResponseDto;
import com.example.event.dto.participant.ParticipantRequestDto;

public interface IParticipantService {

    ParticipantResponseDto create(String eventName);

    ParticipantResponseDto update(Long id, ParticipantRequestDto dto);

    void deleteById(Long id);

    ParticipantResponseDto getById(Long id);

}
