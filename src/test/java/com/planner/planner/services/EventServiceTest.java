package com.planner.planner.services;

import com.planner.planner.repositories.EventRepository;
import com.planner.planner.repositories.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock private EventRepository eventRepository;
    @Mock private UserService userService;
    @Mock private GroupRepository groupRepository;
    private EventService underTest;


    @BeforeEach
    void setUp(){
        underTest = new EventService(eventRepository, userService, groupRepository);
    }

    @Test
    void CheckIfDateIsCorrect(){
        //given
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(5);

        //when
        Boolean result = underTest.checkIfEventTimeIsCorrect(start, end);

        //then
        assertThat(result).isTrue();
    }
}
