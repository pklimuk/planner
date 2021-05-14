package com.planner.planner.repositories;

import com.planner.planner.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long>{
    @Query("SELECT up FROM UserProfile up WHERE up.email = ?1")
    Optional<UserProfile> findUserProfileByEmail(String email);
}