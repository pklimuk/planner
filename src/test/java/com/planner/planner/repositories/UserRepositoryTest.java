package com.planner.planner.repositories;

import com.planner.planner.models.User;
import com.planner.planner.models.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindUserByLogin() {
        //given
        String login = "userLogin";
        String password = "userPassword";
        UserRole role = UserRole.USER;

        User user = new User(login, password, role);
        underTest.save(user);

        //when
        Optional<User> returnedUser = underTest.findUserByLogin(login);

        //then
        assertThat(returnedUser.get()).isEqualTo(user);
    }

    @Test
    void itShouldNotFindUserByLogin() {
        //given
        String login = "userLogin";

        //when
        Optional<User> returnedUser = underTest.findUserByLogin(login);

        //then
        assertThat(returnedUser).isEqualTo(Optional.empty());
    }

    @Test
    void itShouldCheckIfUserIsEnabled() {
        //given
        String login = "userLogin";
        String password = "userPassword";
        UserRole role = UserRole.USER;

        User user = new User(login, password, role);
        underTest.save(user);

        //when
        int enablingResult = underTest.enableUser(login);

        //then
        assertThat(enablingResult).isEqualTo(1);
    }
}