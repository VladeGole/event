package com.example.event.controller;

import com.example.event.dto.event.EventResponseDto;
import com.example.event.dto.event.RequestEvent;
import com.example.event.dto.event.UpdateRequestEventDto;
import com.example.event.dto.response.ApiResponseDto;
import com.example.event.service.event.IEventService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/events")
@Tag(name = "Event")
public class EventController {

    private final IEventService eventService;

    @Autowired
    public EventController(IEventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponseDto> create(@RequestBody RequestEvent event) {
        EventResponseDto eventResponseDto = eventService.create(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDto("Event is create", eventResponseDto));
    }

    @PatchMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponseDto> update(@PathVariable Long id, @RequestBody UpdateRequestEventDto event) {
        EventResponseDto eventResponseDto = eventService.update(id, event);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("Event is update", eventResponseDto));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto> getById(@PathVariable Long id) {
        EventResponseDto eventResponseDto = eventService.getEventById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("Event is found", eventResponseDto));
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponseDto> getAllEvent(){
        List<EventResponseDto> list = eventService.findAllEvent();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("Events are found",list));
    }

    @GetMapping("/by-country/{country}")
    public ResponseEntity<ApiResponseDto> getEventByCountry(@PathVariable String country) {
        List<EventResponseDto> eventResponseDto = eventService.findEventByCountry(country);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("Events are found", eventResponseDto));
    }

    @GetMapping("/by-participant/{participantId}")
    public ResponseEntity<ApiResponseDto> getEventByParticipantId(@PathVariable Long participantId) {
        List<EventResponseDto> eventResponseDto = eventService.findEventByParticipantId(participantId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("Participant is found", eventResponseDto));
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<ApiResponseDto> getEventByName(@PathVariable String name){
        EventResponseDto eventResponseDto = eventService.findEventByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("Events are found", eventResponseDto));
    }

    @GetMapping("/by-private/{isPrivate}")
    public ResponseEntity<ApiResponseDto> getEventByPrivate(@PathVariable boolean isPrivate) {
        List<EventResponseDto> eventResponseDto = eventService.findEventByPrivate(isPrivate);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("Events are found", eventResponseDto));
    }

    @GetMapping("/by-price")
    public ResponseEntity<ApiResponseDto> getEventByPrice(@RequestParam Integer price,
                                                          @RequestParam(defaultValue = "eq") String operator) {
        List<EventResponseDto> eventResponseDto = eventService.findEventByPriceFilter(price, operator);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("Events are found", eventResponseDto));
    }




}
