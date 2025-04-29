package com.example.event.mapper;

import com.example.event.dto.event.EventResponseDto;
import com.example.event.dto.event.RequestEvent;
import com.example.event.dto.event.UpdateRequestEventDto;
import com.example.event.model.Country;
import com.example.event.model.Event;
import com.example.event.model.User;
import com.example.event.repository.CountryRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class EventMapperTest {

    private static EventMapper eventMapper;

    @Mock
    private  CountryRepository countryRepository;

    @BeforeAll
    static void beforeAll() {
        eventMapper = Mappers.getMapper(EventMapper.class);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldMapEventFromRequestDto(){

        RequestEvent dto = new RequestEvent();
        dto.setName("Tech Hokey");
        dto.setDescription("Learning hockey");
        dto.setPrice(300);
        dto.setCurrency("USD");
        dto.setStartDate(LocalDate.parse("2025-05-11"));
        dto.setEndDate(LocalDate.parse("2025-08-12"));
        dto.setCountry("Serbia");
        dto.setEventPrivate(true);

        Country country = new Country("Serbia");

        when(countryRepository.findByName(dto.getCountry())).thenReturn(country);

        Event newEvent = eventMapper.fromRequestDTO(dto, countryRepository);

        assertEquals(dto.getName(), newEvent.getName());
        assertEquals(dto.getDescription(), newEvent.getDescription());
        assertEquals(dto.getPrice(), newEvent.getPrice());
        assertEquals(dto.getCurrency(), newEvent.getCurrency());
        assertEquals(dto.getStartDate(), newEvent.getStartDate());
        assertEquals(dto.getEndDate(), newEvent.getEndDate());
        assertEquals(dto.isEventPrivate(), newEvent.isEventPrivate());
        assertEquals(dto.getCountry(), newEvent.getCountry().getName());

    }

    @Test
    public void shouldMapToDto(){

        Country country = new Country("Serbia");

        User user = new User();
        user.setFirstName("Vlade");
        user.setLastName("Gole");

        Event event = new Event();
        event.setId(1L);
        event.setName("Tech english");
        event.setDescription("Learning english");
        event.setPrice(300);
        event.setCurrency("USD");
        event.setStartDate(LocalDate.parse("2025-05-08"));
        event.setEndDate(LocalDate.parse("2025-07-08"));
        event.setCountry(country);
        event.setOwner(user);

        EventResponseDto dto = eventMapper.toDTO(event);

        assertEquals(event.getId(), dto.getId());
        assertEquals(event.getName(), dto.getName());
        assertEquals(event.getPrice(), dto.getPrice());
        assertEquals(event.getCurrency(), dto.getCurrency());
        assertEquals(event.getStartDate(), dto.getStartDate());
        assertEquals(event.getEndDate(), dto.getEndDate());
        assertEquals(event.getCountry().getName(), dto.getCountry());
        assertEquals(String.join(" ", event.getOwner().getFirstName(),event.getOwner().getLastName()), dto.getOwner());

    }

    @Test
    public void shouldMapEventUpdateFromUpdateRequestEvent(){

        Event original = new Event();
        original.setName("Tech basketball");
        original.setDescription("Learning basketball");
        original.setPrice(300);
        original.setCurrency("USD");
        original.setStartDate(LocalDate.parse("2025-06-08"));
        original.setEndDate(LocalDate.parse("2025-09-08"));
        original.setEventPrivate(false);

        UpdateRequestEventDto dto = new UpdateRequestEventDto();
        dto.setName("Tech Programing");
        dto.setDescription(null);
        dto.setPrice(null);
        dto.setCurrency("USD");
        dto.setStartDate(LocalDate.parse("2025-05-08"));
        dto.setEndDate(LocalDate.parse("2025-08-08"));
        dto.setEventPrivate(false);

        Event updateEvent = eventMapper.updateEvent(dto, original);

        assertEquals(dto.getName(), updateEvent.getName());
        assertEquals("Learning basketball", updateEvent.getDescription());
        assertEquals(300, updateEvent.getPrice());
        assertEquals(dto.getCurrency(), updateEvent.getCurrency());
        assertEquals(dto.getStartDate(), updateEvent.getStartDate());
        assertEquals(dto.getEndDate(), updateEvent.getEndDate());
        assertEquals(dto.isEventPrivate(), updateEvent.isEventPrivate());

    }
}