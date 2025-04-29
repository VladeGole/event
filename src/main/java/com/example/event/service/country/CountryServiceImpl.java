package com.example.event.service.country;

import com.example.event.core.exception.CountryNotFoundExeception;
import com.example.event.core.exception.ObjectAlreadyExistException;
import com.example.event.dto.country.CountryRequestDto;
import com.example.event.dto.country.CountryResponseDto;
import com.example.event.mapper.CountryMapper;
import com.example.event.model.Country;
import com.example.event.model.Role;
import com.example.event.model.User;
import com.example.event.repository.CountryRepository;
import com.example.event.validation.ValidationObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements ICountryService{

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;
    private final ValidationObject<CountryRequestDto> validationObject;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, CountryMapper countryMapper, ValidationObject<CountryRequestDto> validationObject) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
        this.validationObject = validationObject;
    }

    @Override
    public CountryResponseDto createCountry(CountryRequestDto countryRequestDto) {

        validationObject.validate(countryRequestDto);

        Optional.ofNullable(countryRepository.findByName(countryRequestDto.getName())).ifPresent(country -> {throw new ObjectAlreadyExistException();});

        checkUserContainsRole(Role.ADMINISTRATOR);

        Country country = countryMapper.fromRequestDto(countryRequestDto);
        countryRepository.save(country);
        return countryMapper.toDto(country);
    }

    @Override
    public CountryResponseDto updateCountry(Long id, CountryRequestDto countryRequestDto) {

        validationObject.validate(countryRequestDto);

        checkUserContainsRole(Role.ADMINISTRATOR);

        Country country = countryRepository.findById(id).orElseThrow(CountryNotFoundExeception::new);

        countryRepository.findByNameAndIdNot(countryRequestDto.getName(), id).ifPresent(c -> {throw new ObjectAlreadyExistException();});

        Country updateCountry = countryMapper.updateCountry(countryRequestDto,country);

        countryRepository.save(updateCountry);

        return countryMapper.toDto(updateCountry);
    }

    @Override
    public void deleteById(Long id) {

       checkUserContainsRole(Role.ADMINISTRATOR);

        countryRepository.findById(id).ifPresentOrElse(countryRepository::delete, CountryNotFoundExeception::new);
    }

    @Override
    public CountryResponseDto findById(Long id) {
        Country country = countryRepository.findById(id).orElseThrow(CountryNotFoundExeception::new);
        return countryMapper.toDto(country);
    }

    @Override
    public List<CountryResponseDto> findAllCountry() {
        return countryRepository.findAll().stream().map(countryMapper::toDto).toList();
    }


    private void checkUserContainsRole(Role role) {
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        User user= (User) auth.getPrincipal();

        if(!user.getRole().equals(role)){
            throw new AccessDeniedException("You are not authorized to create this event");
        }
    }
}
