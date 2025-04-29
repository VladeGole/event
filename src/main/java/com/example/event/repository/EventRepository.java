package com.example.event.repository;

import com.example.event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {


    List<Event> findByCountryName(String name);

    List<Event> findByPrice(Integer price);

    List<Event> findByEventPrivate(boolean eventPrivate);

    List<Event> findByPriceGreaterThan(Integer price);

    List<Event> findByPriceLessThan(Integer price);

    List<Event> findByPriceGreaterThanEqual(Integer price);

    List<Event> findByPriceLessThanEqual(Integer price);

     Optional<Event> findByNameIgnoreCase(String name);

    List<Event> findByParticipantList_Id(Long participantId);
}
