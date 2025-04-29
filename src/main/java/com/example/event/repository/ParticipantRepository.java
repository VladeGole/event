package com.example.event.repository;

import com.example.event.model.Participant;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant,Long> {

    Participant findByEmail(String email);

    Optional<Participant> findByEmailAndIdNot(@Email String email, Long id);
}
