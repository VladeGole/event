package com.example.event.service.country;

import com.example.event.dto.country.CountryRequestDto;
import com.example.event.dto.country.CountryResponseDto;
import com.example.event.mapper.CountryMapper;
import com.example.event.model.Country;
import com.example.event.model.Role;
import com.example.event.model.User;
import com.example.event.repository.CountryRepository;
import com.example.event.validation.ValidationObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CountryServiceImplTest {
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private CountryMapper countryMapper;
    @Mock
    private ValidationObject validationObject;

    @InjectMocks
    private CountryServiceImpl countryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        User user = new User();
        user.setFirstName("Vlade");
        user.setLastName("Gole");
        user.setId(1L);
        user.setRole(Role.ADMINISTRATOR);

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void shouldSuccessfullyCreateCountry() {
        CountryRequestDto requestDto = new CountryRequestDto();
        requestDto.setName("Serbia");

        Country country = new Country();
        country.setName(requestDto.getName());

        Mockito.when(countryMapper.fromRequestDto(requestDto)).thenReturn(country);
        Mockito.when(countryRepository.save(country)).thenReturn(country);

        CountryResponseDto dto = new CountryResponseDto();
        dto.setId(country.getId());
        dto.setName(country.getName());

        Mockito.when(countryMapper.toDto(country)).thenReturn(dto);

        CountryResponseDto newCountryResponseDto = countryService.createCountry(requestDto);

        assertEquals(newCountryResponseDto.getId(), dto.getId());
        assertEquals(newCountryResponseDto.getName(), dto.getName());

        Mockito.verify(countryMapper, Mockito.times(1)).fromRequestDto(requestDto);
        Mockito.verify(countryRepository, Mockito.times(1)).save(country);
        Mockito.verify(countryMapper, Mockito.times(1)).toDto(country);

    }

    @Test
    public void shouldSuccessfullyUpdateCountry(){

        Long id = 4L;

        CountryRequestDto requestDto = new CountryRequestDto();
        requestDto.setName("Serbia");

        Country country = new Country();
        country.setId(4L);
        country.setName("Portugal");

        Mockito.when(countryRepository.findById(id)).thenReturn(Optional.of(country));
        Mockito.when(countryRepository.findByNameAndIdNot(requestDto.getName(), id)).thenReturn(Optional.empty());

        Country updateCountry = new Country();
        updateCountry.setId(id);
        updateCountry.setName(requestDto.getName());

        Mockito.when(countryMapper.updateCountry(requestDto, country)).thenReturn(updateCountry);

        Mockito.when(countryRepository.save(updateCountry)).thenReturn(updateCountry);

        CountryResponseDto dto = new CountryResponseDto();
        dto.setId(updateCountry.getId());
        dto.setName(updateCountry.getName());

        Mockito.when(countryMapper.toDto(updateCountry)).thenReturn(dto);

        CountryResponseDto newCountryResponseDto = countryService.updateCountry(id, requestDto);

        assertEquals(newCountryResponseDto.getId(), dto.getId());
        assertEquals(newCountryResponseDto.getName(), dto.getName());

        Mockito.verify(countryRepository, Mockito.times(1)).findById(id);
        Mockito.verify(countryRepository, Mockito.times(1)).findByNameAndIdNot(requestDto.getName(), id);
        Mockito.verify(countryMapper, Mockito.times(1)).updateCountry(requestDto, country);
        Mockito.verify(countryRepository, Mockito.times(1)).save(updateCountry);
        Mockito.verify(countryMapper, Mockito.times(1)).toDto(updateCountry);

    }

    @Test
    public void shouldReturnCountryById(){

        Long id = 4L;
        Country country = new Country();
        country.setId(4L);
        country.setName("Portugal");

        Mockito.when(countryRepository.findById(id)).thenReturn(Optional.of(country));

        CountryResponseDto dto = new CountryResponseDto();
        dto.setId(country.getId());
        dto.setName(country.getName());

        Mockito.when(countryMapper.toDto(country)).thenReturn(dto);

        CountryResponseDto countryResponseDto = countryService.findById(id);

        assertEquals(countryResponseDto.getId(), dto.getId());
        assertEquals(countryResponseDto.getName(), dto.getName());

        Mockito.verify(countryRepository, Mockito.times(1)).findById(id);
        Mockito.verify(countryMapper, Mockito.times(1)).toDto(country);
    }
}