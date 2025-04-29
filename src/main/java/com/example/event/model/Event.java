package com.example.event.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The name field must be filled in")
    private String name;

    @NotBlank(message = "The description field must be filled in")
    private String description;

    @ManyToMany()
    @JoinTable(
            name = "event_participant",
            joinColumns = @JoinColumn(name ="event_id" ),
            inverseJoinColumns = @JoinColumn(name="participant_id")
    )
    private Set<Participant> participantList;

    @Positive(message = "Price must be positive")
    @NotNull(message = "The price field not must be null ")
    @Min(value = 50, message = "Price must be at least 50")
    private Integer price;

    @NotBlank(message = "The currency field must be filled in")
    private String currency;

    @NotNull(message = "Date is required")
    private LocalDate startDate;

    @NotNull(message = "Date is required")
    private LocalDate endDate;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne()
    @JoinColumn(name = "owner_id")
    private User owner;

    private boolean eventPrivate;
}
