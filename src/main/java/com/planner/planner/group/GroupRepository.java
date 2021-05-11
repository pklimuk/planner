package com.planner.planner.group;

import com.planner.planner.deadline.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("SELECT up FROM Groups up WHERE up.group_name = ?1 and up.group_user.id = ?2")
    Optional<Group> findGroupByNameAndUserId(String name, Long user_id);
}