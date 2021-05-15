package com.planner.planner;

import com.planner.planner.repositories.DeadlineRepository;
import com.planner.planner.repositories.GroupRepository;
import com.planner.planner.repositories.UserRepository;
import com.planner.planner.services.DeadlineService;
import com.planner.planner.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

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
    void CheckDateIsCorrect() {
        LocalDateTime new_time = LocalDateTime.now().plusDays(1L);

        Boolean result = underTest.checkIfDeadlineTimeIsCorrect(new_time);

        assert result.equals(true);
    }
}
