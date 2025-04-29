package com.example.event.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "participant")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The first name field must be filled in")
    private String firstName;

    @NotBlank(message = "The last name field must be filled in")
    private String lastName;

    @Email
    private String email;

    @Builder.Default
    @ManyToMany(mappedBy = "participantList", cascade = CascadeType.MERGE)
    private Set<Event> eventList = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "user_id")
    private User participant;
}
