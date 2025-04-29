package com.example.event.controller;

import com.example.event.dto.participant.ParticipantRequestDto;
import com.example.event.dto.participant.ParticipantResponseDto;
import com.example.event.dto.response.ApiResponseDto;
import com.example.event.service.participant.IParticipantService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/participants")
@Tag(name = "Participant")
public class ParticipantController {


    private final IParticipantService participantService;

    @Autowired
    public ParticipantController(IParticipantService participantService) {
        this.participantService = participantService;
    }

    @PostMapping("/{eventName}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponseDto> create(@PathVariable String eventName) {
        ParticipantResponseDto participantResponseDto = participantService.create( eventName);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("Participant is create", participantResponseDto));
    }

    @PatchMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponseDto> update(@PathVariable Long id, @RequestBody ParticipantRequestDto participantRequestDto) {
        ParticipantResponseDto participantResponseDto = participantService.update(id, participantRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("Participant update", participantResponseDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto> getById(@PathVariable Long id) {
        ParticipantResponseDto participantResponseDto = participantService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("Participant is found", participantResponseDto));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
         participantService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
