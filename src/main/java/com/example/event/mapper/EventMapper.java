package com.example.event.mapper;

import com.example.event.dto.event.RequestEvent;
import com.example.event.dto.event.EventResponseDto;
import com.example.event.dto.event.UpdateRequestEventDto;
import com.example.event.model.Country;
import com.example.event.model.Event;
import com.example.event.model.User;
import com.example.event.repository.CountryRepository;
import org.mapstruct.*;

import java.util.Objects;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventMapper {


    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target="price")
    @Mapping(source = "currency", target = "currency")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "eventPrivate", target = "eventPrivate")
    @Mapping(target = "country", expression = "java(countryEntity(event.getCountry(), countryRepository))" )
    Event fromRequestDTO(RequestEvent event, @Context CountryRepository countryRepository);

    default Country countryEntity(String name, CountryRepository countryRepository){
        return countryRepository.findByName(name);
    }

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "currency", target = "currency")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "country", target = "country", qualifiedByName = "toCountryName")
    @Mapping(source = "owner", target = "owner", qualifiedByName = "toOwnerName")
    EventResponseDto toDTO(Event event);

    @Named("toOwnerName")
    default String nameOwner(User user){
        return Objects.isNull(user)? null : String.join(" ", user.getFirstName(), user.getLastName());
    }

    @Named("toCountryName")
    default String countryName(Country country){
        return Objects.isNull(country)? null : country.getName();
    }

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target= "price")
    @Mapping(source = "currency", target = "currency")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "eventPrivate", target = "eventPrivate")
    Event updateEvent(UpdateRequestEventDto updateEvent, @MappingTarget Event event);
}
