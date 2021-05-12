package com.planner.planner.deadline;

import com.planner.planner.deadline.Deadline;
import com.planner.planner.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DeadlineRepository extends JpaRepository<Deadline, Long> {
    @Query("SELECT up FROM Users_Deadlines up WHERE up.title = ?1 and up.deadline_user.id = ?2")
    Optional<Deadline> findDeadlineByTitleAndUserId(String title, Long user_id);

    @Query("SELECT up FROM Users_Deadlines up WHERE up.title = ?1 and up.deadline_user.id = ?2")
    List<Optional<Deadline>> findListOfDeadlineByTitleAndUserId(String title, Long user_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Users_Deadlines deadline WHERE deadline.id =?1")
    void deleteDeadlineById(Long Id);
}
