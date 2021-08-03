package com.planner.planner.services;

import com.planner.planner.repositories.DeadlineRepository;
import com.planner.planner.repositories.GroupRepository;
import com.planner.planner.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class DeadlineServiceTest {

    @Mock private DeadlineRepository deadlineRepository;
    @Mock private UserService userService;
    @Mock private UserRepository userRepository;
    @Mock private GroupRepository groupRepository;
    private DeadlineService underTest;

    @BeforeEach
    void setUp(){
        underTest = new DeadlineService(deadlineRepository, userService,
                userRepository, groupRepository);
    }


    @Test
    void CheckIfDateIsCorrect() {
        //given
        LocalDateTime new_time = LocalDateTime.now().plusDays(1L);

        //when
        Boolean result = underTest.checkIfDeadlineTimeIsCorrect(new_time);

        //then
        assertThat(result).isTrue();
    }
}
