package com.example.event.repository;

import com.example.event.model.Country;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country,Long> {
    Country findByName(String name);

    Optional<Country> findByNameAndIdNot(@NotBlank(message = "The name field must be filled in") String name, Long id);
}
