package com.planner.planner.user;

import com.planner.planner.user.User;
import com.planner.planner.userProfile.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT up FROM Users up WHERE up.login = ?1")
    Optional<User> findUserByLogin(String login);

    @Transactional
    @Modifying
    @Query("UPDATE Users a " + "SET a.enabled = TRUE WHERE a.login = ?1")
    int enableUser(String login);
}
