package com.planner.planner.deadline;

import com.planner.planner.deadline.Deadline;
import com.planner.planner.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DeadlineRepository extends JpaRepository<Deadline, Long> {
    @Query("SELECT up FROM Users_Deadlines up WHERE up.title = ?1")
    Optional<Deadline> findDeadlineByTitle(String title);

    @Query("SELECT up FROM Users_Deadlines up WHERE up.title = ?1")
    List<Optional<Deadline>> findListOfDeadlineByTitle(String title);
}
