package com.planner.planner.deadline;

import com.planner.planner.deadline.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface DeadlineRepository extends JpaRepository<Deadline, Long> {
}
