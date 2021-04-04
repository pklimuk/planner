package com.planner.planner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository  extends PagingAndSortingRepository<UserProfile, Long> {
    @Query("SELECT s FROM UserProfile s WHERE s.email = ?1")
    Optional<UserProfile> findUserProfileByEmail(String email);

    @Query("SELECT s FROM UserProfile s WHERE s.firstName = ?1")
    List<UserProfile> selectUserProfileWhereFirstName(
            String firstName, Integer age);


    @Transactional
    @Modifying
    @Query("DELETE FROM UserProfile u WHERE u.id = ?1")
    int deleteUserProfileById(Long id);
}

//public interface UserProfileRepository extends JpaRepository<UserProfile, Long>