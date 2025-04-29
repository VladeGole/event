package com.example.event.service.participant;

import com.example.event.core.exception.EventNotFoundException;
import com.example.event.core.exception.ObjectAlreadyExistException;
import com.example.event.core.exception.ParticipantNotFoundException;
import com.example.event.dto.participant.ParticipantResponseDto;
import com.example.event.dto.participant.ParticipantRequestDto;
import com.example.event.mapper.EventMapper;
import com.example.event.mapper.ParticipantMapper;
import com.example.event.model.Event;
import com.example.event.model.Participant;
import com.example.event.model.Role;
import com.example.event.model.User;
import com.example.event.repository.EventRepository;
import com.example.event.repository.ParticipantRepository;
import com.example.event.validation.ValidationObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParticipantServiceImpl implements IParticipantService {

    private final ParticipantRepository participantRepository;
    private final ParticipantMapper mapper;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final ValidationObject<Participant>validationObject;

    @Autowired
    public ParticipantServiceImpl(ParticipantRepository participantRepository, ParticipantMapper mapper, EventRepository eventRepository, EventMapper eventMapper, ValidationObject<Participant> validationObject) {
        this.participantRepository = participantRepository;
        this.mapper = mapper;
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.validationObject = validationObject;
    }

    @Override
    public ParticipantResponseDto create(String eventName) {

        User user = getAuthenticatedUser();

        if(!user.getRole().equals(Role.PARTICIPANT)) {
            throw new AccessDeniedException("You are not authorized to create this event");
        }

        Event event = eventRepository.findByNameIgnoreCase(eventName).orElseThrow(EventNotFoundException::new);


        Participant participant = Participant.builder().firstName(user.getFirstName()).lastName(user.getLastName())
                .email(user.getEmail()).participant(user).build();


        participant.getEventList().add(event);

        event.getParticipantList().add(participant);

        participantRepository.save(participant);

        return mapper.toDto(participant);
    }

    @Override
    public ParticipantResponseDto update(Long id, ParticipantRequestDto dto) {

        User user = getAuthenticatedUser();

        if(!user.getRole().equals(Role.PARTICIPANT)) {
            throw new AccessDeniedException("You are not authorized to update this event");
        }

        Participant participant = participantRepository.findById(id).orElseThrow(ParticipantNotFoundException::new);

        if(!participant.getParticipant().getId().equals(user.getId())){
            throw new AccessDeniedException("You are not authorized to update this event");
        }

        participantRepository.findByEmailAndIdNot(dto.getEmail(),id).ifPresent(p -> { throw new ObjectAlreadyExistException(); });

        Participant updateParticipant = mapper.updateFromRequest(dto,participant);

        validationObject.validate(updateParticipant);

        participantRepository.save(updateParticipant);

        return mapper.toDto(updateParticipant);
    }


    @Transactional
    @Override
    public void deleteById(Long id) {

        checkUserContainsRole(Role.PARTICIPANT);

        Participant participant = participantRepository.findById(id)
                .orElseThrow(ParticipantNotFoundException::new);

        for (Event event : participant.getEventList()) {
            event.getParticipantList().remove(participant);
        }

        participant.getEventList().clear();

        participantRepository.delete(participant);
    }

    @Override
    public ParticipantResponseDto getById(Long id) {
        return participantRepository.findById(id).map(mapper::toDto).orElseThrow(ParticipantNotFoundException::new);
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }

    private void checkUserContainsRole(Role role) {
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        User user= (User) auth.getPrincipal();

        if(!user.getRole().equals(role)){
            throw new AccessDeniedException("You are not authorized to create this event");
        }
    }
}
