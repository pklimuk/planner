package com.planner.planner.repositories;

import com.planner.planner.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT up FROM Users_Events up WHERE up.title = ?1 and up.event_user.id = ?2")
    List<Optional<Event>> findListOfEventsByTitleAndUserId(String title, Long user_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Users_Events event WHERE event.id =?1")
    void deleteEventById(Long Id);
}
