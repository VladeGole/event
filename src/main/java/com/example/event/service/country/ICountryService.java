package com.example.event.service.country;

import com.example.event.dto.country.CountryRequestDto;
import com.example.event.dto.country.CountryResponseDto;

import java.util.List;

public interface ICountryService {

    CountryResponseDto createCountry(CountryRequestDto requestDto);

    CountryResponseDto updateCountry(Long id, CountryRequestDto countryRequestDto);

    void deleteById(Long id);

    CountryResponseDto findById(Long id);

    List<CountryResponseDto> findAllCountry();
}
