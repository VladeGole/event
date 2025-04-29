package com.example.event.service.event;

import com.example.event.dto.event.EventResponseDto;
import com.example.event.dto.event.RequestEvent;
import com.example.event.dto.event.UpdateRequestEventDto;

import java.util.List;

public interface IEventService {

     EventResponseDto create(RequestEvent requestEvent);

     EventResponseDto update(Long id, UpdateRequestEventDto updateEvent);

     void deleteEvent(Long id);

     EventResponseDto getEventById(Long id);

     List<EventResponseDto> findAllEvent();

     EventResponseDto findEventByName(String name);

     List<EventResponseDto> findEventByCountry(String name);

    List<EventResponseDto> findEventByParticipantId(Long id);

    List<EventResponseDto> findEventByPriceFilter(Integer price, String operator);

     List<EventResponseDto> findEventByPrivate(boolean eventPrivate);




}
