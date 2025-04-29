package com.example.event.mapper;

import com.example.event.dto.country.CountryRequestDto;
import com.example.event.dto.country.CountryResponseDto;
import com.example.event.model.Country;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class CountryMapperTest {

    private static CountryMapper countryMapper;

    @BeforeAll
    static void beforeAll() {
        countryMapper= Mappers.getMapper(CountryMapper.class);
    }

    @Test
    public void shouldMapCountryToDto() {
        Country country = new Country("Serbia");

        CountryResponseDto dto = countryMapper.toDto(country);

        assertEquals(country.getName(), dto.getName());
    }

    @Test
    public void shouldMapRequestCountryDtoToCountry() {
        CountryRequestDto dto = new CountryRequestDto();
        dto.setName("Germany");

        Country country = countryMapper.fromRequestDto(dto);

        assertEquals(dto.getName(), country.getName());
    }

    @Test
    public void shouldUpdateCountryFromCountryRequest() {

        CountryRequestDto countryDto = new CountryRequestDto();

        countryDto.setName("Portugal");

        Country country = new Country("Serbia");

        countryMapper.updateCountry(countryDto, country);

        assertEquals(countryDto.getName(), country.getName());

    }

    @Test
    public void shouldUpdateCountryFromRequestIgnoreNull() {

        CountryRequestDto countryDto = new CountryRequestDto();

        countryDto.setName(null);

        Country country = new Country("Serbia");

        countryMapper.updateCountry(countryDto, country);

        assertEquals("Serbia", country.getName());
    }
}