package com.example.event.service.participant;

import com.example.event.dto.participant.ParticipantResponseDto;
import com.example.event.mapper.ParticipantMapper;
import com.example.event.model.*;
import com.example.event.repository.EventRepository;
import com.example.event.repository.ParticipantRepository;
import com.example.event.validation.ValidationObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantServiceImplTest {
    @Mock
    private ParticipantRepository participantRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private ParticipantMapper mapper;
    @Mock
    private ValidationObject validationObject;
    @InjectMocks
    private ParticipantServiceImpl participantService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        User user = new User(1L, "Vlade", "Gole", "vlade@gmail.com", "lozinka", Role.PARTICIPANT);

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void shouldSuccessfullCreateParticipant() {
        String name = "Tech English";

        Event event = new Event();
        event.setId(4L);
        event.setName("Tech English");
        event.setDescription("Learning English");
        event.setPrice(300);
        event.setCurrency("USD");
        event.setStartDate(LocalDate.parse("2025-05-11"));
        event.setEndDate(LocalDate.parse("2025-08-12"));
        event.setCountry(new Country("Serbia"));
        event.setEventPrivate(true);
        event.setParticipantList(new HashSet<>());

        Mockito.when(eventRepository.findByNameIgnoreCase(name)).thenReturn(Optional.of(event));

        User user = new User(1L, "Vlade", "Gole", "vlade@gmail.com", "lozinka", Role.PARTICIPANT);

        Participant participant = Participant.builder().id(10L).firstName(user.getFirstName()).lastName(user.getLastName())
                .email(user.getEmail()).participant(user).build();


        participant.getEventList().add(event);
        event.getParticipantList().add(participant);

        Mockito.when(participantRepository.save(participant)).thenReturn(participant);

        ParticipantResponseDto dto = new ParticipantResponseDto();
        dto.setId(participant.getId());
        dto.setEmail(participant.getEmail());

        Mockito.when(mapper.toDto(Mockito.any(Participant.class))).thenReturn(dto);

        ParticipantResponseDto participantResponseDto = participantService.create(name);

        assertEquals(participantResponseDto.getId(), dto.getId());
        assertEquals(participantResponseDto.getEmail(), dto.getEmail());

        Mockito.verify(eventRepository, Mockito.times(1)).findByNameIgnoreCase(name);
        Mockito.verify(participantRepository, Mockito.times(1)).save(Mockito.any(Participant.class));
        Mockito.verify(mapper, Mockito.times(1)).toDto(Mockito.any(Participant.class));
    }

    @Test
    public void shouldReturnParticipantById() {

        Long id = 2L;

        User user = new User(1L, "Vlade", "Gole", "vlade@gmail.com", "lozinka", Role.PARTICIPANT);

        Participant participant = Participant.builder().id(id).firstName(user.getFirstName()).lastName(user.getLastName())
                .email(user.getEmail()).participant(user).build();

        Mockito.when(participantRepository.findById(id)).thenReturn(Optional.ofNullable(participant));

        ParticipantResponseDto dto = new ParticipantResponseDto();
        dto.setId(participant.getId());
        dto.setEmail(participant.getEmail());

        Mockito.when(mapper.toDto(Mockito.any(Participant.class))).thenReturn(dto);

        ParticipantResponseDto participantResponseDto = participantService.getById(id);

        assertEquals(participantResponseDto.getId(), dto.getId());
        assertEquals(participantResponseDto.getEmail(), dto.getEmail());


        Mockito.verify(participantRepository, Mockito.times(1)).findById(id);
        Mockito.verify(mapper, Mockito.times(1)).toDto(Mockito.any(Participant.class));

    }
}