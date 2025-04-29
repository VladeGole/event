package com.example.event.service.event;

import com.example.event.dto.event.EventResponseDto;
import com.example.event.dto.event.RequestEvent;
import com.example.event.dto.event.UpdateRequestEventDto;
import com.example.event.mapper.EventMapper;
import com.example.event.model.Country;
import com.example.event.model.Event;
import com.example.event.model.Role;
import com.example.event.model.User;
import com.example.event.repository.CountryRepository;
import com.example.event.repository.EventRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventMapper eventMapper;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private ValidationObject validationObject;

    @InjectMocks
    private EventServiceImpl eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        User user = new User();
        user.setFirstName("Vlade");
        user.setLastName("Gole");
        user.setId(1L);
        user.setRole(Role.ADMIN);

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void shouldSuccessfullySaveEvent() {

        RequestEvent dto = new RequestEvent();
        dto.setName("Tech Hokey");
        dto.setDescription("Learning hockey");
        dto.setPrice(300);
        dto.setCurrency("USD");
        dto.setStartDate(LocalDate.parse("2025-05-11"));
        dto.setEndDate(LocalDate.parse("2025-08-12"));
        dto.setCountry("Serbia");
        dto.setEventPrivate(true);

        Country country = new Country();
        country.setName(dto.getCountry());

        Mockito.when(countryRepository.findByName(dto.getCountry())).thenReturn(null);
        Mockito.when(countryRepository.save(Mockito.any(Country.class))).thenReturn(country);

        Mockito.when(eventRepository.findByNameIgnoreCase(dto.getName())).thenReturn(Optional.empty());


        Event event = new Event();
        event.setId(1L);
        event.setName("Tech Hokey");
        event.setDescription("Learning hockey");
        event.setPrice(300);
        event.setCurrency("USD");
        event.setStartDate(LocalDate.parse("2025-05-11"));
        event.setEndDate(LocalDate.parse("2025-08-12"));
        event.setCountry(country);
        event.setEventPrivate(true);

        User user = new User();
        user.setFirstName("Vlade");
        user.setLastName("Gole");
        user.setId(1L);

        event.setOwner(user);

        Mockito.when(eventMapper.fromRequestDTO(dto, countryRepository)).thenReturn(event);


        Mockito.when(eventRepository.save(event)).thenReturn(event);

        EventResponseDto eventResponseDto = new EventResponseDto();
        eventResponseDto.setId(1L);
        eventResponseDto.setName("Tech Hokey");
        eventResponseDto.setPrice(300);
        eventResponseDto.setCurrency("USD");
        eventResponseDto.setStartDate(LocalDate.parse("2025-05-11"));
        eventResponseDto.setEndDate(LocalDate.parse("2025-08-12"));
        eventResponseDto.setCountry(country.getName());
        eventResponseDto.setOwner(String.join(" ", event.getOwner().getFirstName(), event.getOwner().getLastName()));


        Mockito.when(eventMapper.toDTO(event)).thenReturn(eventResponseDto);

        EventResponseDto newEventResponseDto = eventService.create(dto);

         assertEquals(dto.getName(), eventResponseDto.getName());
         assertEquals(dto.getPrice(), eventResponseDto.getPrice());
         assertEquals(dto.getCurrency(), eventResponseDto.getCurrency());
         assertEquals(dto.getStartDate(), eventResponseDto.getStartDate());
         assertEquals(dto.getEndDate(), eventResponseDto.getEndDate());
         assertEquals(dto.getCountry(), eventResponseDto.getCountry());

        Mockito.verify(countryRepository, Mockito.times(1)).findByName(dto.getCountry());
        Mockito.verify(countryRepository, Mockito.times(1)).save(Mockito.any(Country.class));
        Mockito.verify(eventRepository, Mockito.times(1)).findByNameIgnoreCase(dto.getName());
        Mockito.verify(eventMapper, Mockito.times(1)).fromRequestDTO(dto, countryRepository);
        Mockito.verify(eventRepository, Mockito.times(1)).save(event);

    }

    @Test
    public void shouldSuccessfullyUpdateEvent() {

        Long id = 4L;

        UpdateRequestEventDto dto = new UpdateRequestEventDto();
        dto.setName("Tech Hokey");
        dto.setDescription("Learning hockey");
        dto.setPrice(300);
        dto.setCurrency("USD");
        dto.setStartDate(LocalDate.parse("2025-05-11"));
        dto.setEndDate(LocalDate.parse("2025-08-12"));
        dto.setEventPrivate(true);

        User user = new User(1L, "Vlade", "Gole", "vlade.gole2001@gmail.com", "licnazamenica", Role.ADMIN);

        Event event = new Event();
        event.setId(4L);
        event.setName("Tech Hokey");
        event.setDescription("Learning hockey");
        event.setPrice(300);
        event.setCurrency("USD");
        event.setStartDate(LocalDate.parse("2025-05-11"));
        event.setEndDate(LocalDate.parse("2025-08-12"));
        event.setCountry(new Country("Serbia"));
        event.setEventPrivate(true);
        event.setOwner(user);

        Mockito.when(eventRepository.findById(id)).thenReturn(Optional.of(event));

        Event updateEvent = new Event();
        updateEvent.setId(id);
        updateEvent.setName(dto.getName());
        updateEvent.setDescription(dto.getDescription());
        updateEvent.setPrice(dto.getPrice());
        updateEvent.setCurrency(dto.getCurrency());
        updateEvent.setStartDate(dto.getStartDate());
        updateEvent.setEndDate(dto.getEndDate());
        updateEvent.setEventPrivate(dto.isEventPrivate());
        updateEvent.setCountry(event.getCountry());
        updateEvent.setOwner(event.getOwner());

        Mockito.when(eventMapper.updateEvent(dto,event)).thenReturn(updateEvent);

        Mockito.when(eventRepository.save(updateEvent)).thenReturn(updateEvent);

        EventResponseDto eventResponseDto = new EventResponseDto();
        eventResponseDto.setId(updateEvent.getId());
        eventResponseDto.setName(updateEvent.getName());
        eventResponseDto.setPrice(updateEvent.getPrice());
        eventResponseDto.setCurrency(updateEvent.getCurrency());
        eventResponseDto.setStartDate(updateEvent.getStartDate());
        eventResponseDto.setEndDate(updateEvent.getEndDate());
        eventResponseDto.setCountry(updateEvent.getCountry().getName());
        eventResponseDto.setOwner(String.join(" ", updateEvent.getOwner().getFirstName(), event.getOwner().getLastName()));

        Mockito.when(eventMapper.toDTO(updateEvent)).thenReturn(eventResponseDto);

        EventResponseDto eventResponseDtoUpdate = eventService.update(id, dto);

        assertEquals(eventResponseDto.getId(), eventResponseDtoUpdate.getId());
        assertEquals(eventResponseDto.getName(), eventResponseDtoUpdate.getName());
        assertEquals(eventResponseDto.getPrice(), eventResponseDtoUpdate.getPrice());
        assertEquals(eventResponseDto.getCurrency(), eventResponseDtoUpdate.getCurrency());
        assertEquals(eventResponseDto.getStartDate(), eventResponseDtoUpdate.getStartDate());
        assertEquals(eventResponseDto.getEndDate(), eventResponseDtoUpdate.getEndDate());
        assertEquals(eventResponseDto.getCountry(), eventResponseDtoUpdate.getCountry());
        assertEquals(eventResponseDto.getOwner(), eventResponseDtoUpdate.getOwner());

        Mockito.verify(eventRepository, Mockito.times(1)).findById(id);
        Mockito.verify(eventMapper, Mockito.times(1)).updateEvent(dto, event);
        Mockito.verify(eventRepository, Mockito.times(1)).save(updateEvent);
        Mockito.verify(eventMapper, Mockito.times(1)).toDTO(updateEvent);

    }

    @Test
    public void shouldReturnEventById() {

        Long id = 4L;

        User user = new User(1L, "Vlade", "Gole", "vlade.gole2001@gmail.com", "licnazamenica", Role.ADMIN);

        Event event = new Event();
        event.setId(4L);
        event.setName("Tech Hokey");
        event.setDescription("Learning hockey");
        event.setPrice(300);
        event.setCurrency("USD");
        event.setStartDate(LocalDate.parse("2025-05-11"));
        event.setEndDate(LocalDate.parse("2025-08-12"));
        event.setCountry(new Country("Serbia"));
        event.setEventPrivate(true);
        event.setOwner(user);

        EventResponseDto eventResponseDto = new EventResponseDto();
        eventResponseDto.setId(event.getId());
        eventResponseDto.setName(event.getName());
        eventResponseDto.setPrice(event.getPrice());
        eventResponseDto.setCurrency(event.getCurrency());
        eventResponseDto.setStartDate(event.getStartDate());
        eventResponseDto.setEndDate(event.getEndDate());
        eventResponseDto.setCountry(event.getCountry().getName());
        eventResponseDto.setOwner(String.join(" ", event.getOwner().getFirstName(), event.getOwner().getLastName()));

        Mockito.when(eventRepository.findById(id)).thenReturn(Optional.of(event));
        Mockito.when(eventMapper.toDTO(event)).thenReturn(eventResponseDto);

        EventResponseDto newEventResponseDto = eventService.getEventById(id);

        assertEquals(eventResponseDto.getId(), newEventResponseDto.getId());
        assertEquals(eventResponseDto.getName(), newEventResponseDto.getName());
        assertEquals(eventResponseDto.getPrice(), newEventResponseDto.getPrice());
        assertEquals(eventResponseDto.getCurrency(), newEventResponseDto.getCurrency());
        assertEquals(eventResponseDto.getStartDate(), newEventResponseDto.getStartDate());
        assertEquals(eventResponseDto.getEndDate(), newEventResponseDto.getEndDate());
        assertEquals(eventResponseDto.getCountry(), newEventResponseDto.getCountry());
        assertEquals(eventResponseDto.getOwner(), newEventResponseDto.getOwner());

        Mockito.verify(eventRepository, Mockito.times(1)).findById(id);
        Mockito.verify(eventMapper, Mockito.times(1)).toDTO(event);


    }
}