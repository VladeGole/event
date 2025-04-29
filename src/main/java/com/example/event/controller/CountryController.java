package com.example.event.controller;

import com.example.event.dto.country.CountryResponseDto;
import com.example.event.dto.country.CountryRequestDto;
import com.example.event.dto.response.ApiResponseDto;
import com.example.event.service.country.ICountryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
@Tag(name = "Country")
public class CountryController {

    private final ICountryService countryService;

    @Autowired
    public CountryController(ICountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping("/")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponseDto> create(@RequestBody CountryRequestDto requestDto){
        CountryResponseDto countryResponseDto = countryService.createCountry(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDto("Country is created", countryResponseDto));
    }

    @PatchMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponseDto> update(@PathVariable Long id, @RequestBody CountryRequestDto requestDto){
        CountryResponseDto countryResponseDto = countryService.updateCountry(id,requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("Country is updated", countryResponseDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto> getById(@PathVariable Long id) {
        CountryResponseDto countryResponseDto = countryService.findById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(new ApiResponseDto("Country is found", countryResponseDto));
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponseDto> getAll() {
        List<CountryResponseDto> countryResponseDto = countryService.findAllCountry();
        return ResponseEntity.status(HttpStatus.FOUND).body(new ApiResponseDto("Country is found", countryResponseDto));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        countryService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
