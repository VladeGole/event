package com.example.event.service.event;

import com.example.event.core.exception.EventNotFoundException;
import com.example.event.core.exception.ObjectAlreadyExistException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements IEventService{

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CountryRepository countryRepository;
    private final ValidationObject<RequestEvent>validationObject;
    private final ValidationObject<Event>validationEvent;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper, CountryRepository countryRepository, ValidationObject<RequestEvent> validationObject, ValidationObject<Event> validationEvent) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.countryRepository = countryRepository;
        this.validationObject = validationObject;
        this.validationEvent = validationEvent;
    }

    @Override
    public EventResponseDto create(RequestEvent requestEvent) {
        validationObject.validate(requestEvent);

        Country country = Optional.ofNullable(countryRepository.findByName(requestEvent.getCountry()))
                .orElseGet(()-> {
                    Country eventCountry=new Country(requestEvent.getCountry());
                    return countryRepository.save(eventCountry);
                });

       eventRepository.findByNameIgnoreCase(requestEvent.getName()).ifPresent(e-> {throw new ObjectAlreadyExistException();});


        User user = getAuthenticatedUser();

        if(!user.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("You are not authorized to create this event");
        }

        Event event=eventMapper.fromRequestDTO(requestEvent,countryRepository);

        event.setOwner(user);

        eventRepository.save(event);

        return eventMapper.toDTO(event);
    }

    @Override
    public EventResponseDto update(Long id, UpdateRequestEventDto updateEventRequest) {

        User currentUser = getAuthenticatedUser();

        Event event = eventRepository.findById(id).orElseThrow(EventNotFoundException::new);

        if(!event.getOwner().getId().equals(currentUser.getId())){
             throw new AccessDeniedException("You are not authorized to update this event");
        }

        if(!currentUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("You are not authorized to create this event");
        }

        Event updatedEvent = eventMapper.updateEvent(updateEventRequest,event);

        validationEvent.validate(updatedEvent);

        eventRepository.save(updatedEvent);

        return eventMapper.toDTO(updatedEvent);
    }

    @Override
    public void deleteEvent(Long id) {

        User currentUser = getAuthenticatedUser();

         Event event = eventRepository.findById(id).orElseThrow(EventNotFoundException::new);

        if(!event.getOwner().getId().equals(currentUser.getId())){
            throw new AccessDeniedException("You are not authorized to delete this event");
        }

        if(!currentUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("You are not authorized to create this event");
        }

         eventRepository.delete(event);
    }

    @Override
    public EventResponseDto getEventById(Long id) {
        return eventRepository.findById(id).map(eventMapper::toDTO).orElseThrow(EventNotFoundException::new);
    }

    @Override
    public List<EventResponseDto> findAllEvent() {
        return eventRepository.findAll().stream().map(eventMapper::toDTO).toList();
    }

    @Override
    public EventResponseDto findEventByName(String name) {
        return eventRepository.findByNameIgnoreCase(name.toLowerCase()).map(eventMapper::toDTO).orElseThrow(EventNotFoundException::new);
    }

    @Override
    public List<EventResponseDto> findEventByCountry(String name) {
        return eventRepository.findByCountryName(name).stream().map(eventMapper::toDTO).toList();
    }


    @Override
    public List<EventResponseDto> findEventByPrivate(boolean eventPrivate) {
        return eventRepository.findByEventPrivate(eventPrivate).stream().map(eventMapper::toDTO).toList();
    }

    @Override
    public List<EventResponseDto> findEventByParticipantId(Long id) {
        return eventRepository.findByParticipantList_Id(id).stream().map(eventMapper::toDTO).toList();
    }

    @Override
    public List<EventResponseDto> findEventByPriceFilter(Integer price, String operator) {
        List<Event>events = switch (operator.toLowerCase()) {
            case "gt" -> eventRepository.findByPriceGreaterThan(price);
            case "lt" -> eventRepository.findByPriceLessThan(price);
            case "gte" -> eventRepository.findByPriceGreaterThanEqual(price);
            case "lte" -> eventRepository.findByPriceLessThanEqual(price);
            case "eq" -> eventRepository.findByPrice(price);
            default -> throw new IllegalArgumentException("The parameters are incorrect");
        };

        return events.stream().map(eventMapper::toDTO).toList();
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }


}
