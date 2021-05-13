package com.planner.planner.event;

import com.planner.planner.deadline.Deadline;
import com.planner.planner.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
