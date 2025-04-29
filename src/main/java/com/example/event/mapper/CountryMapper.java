package com.example.event.mapper;

import com.example.event.dto.country.CountryResponseDto;
import com.example.event.dto.country.CountryRequestDto;
import com.example.event.model.Country;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CountryMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    CountryResponseDto toDto(Country country);

    @InheritInverseConfiguration(name = "toDto")
    Country fromDto(CountryResponseDto countryResponseDto);

    @Mapping(source = "name", target = "name", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Country updateCountry(CountryRequestDto dto, @MappingTarget Country country);

    @Mapping(source = "name", target = "name")
    Country fromRequestDto(CountryRequestDto requestDto);
}
