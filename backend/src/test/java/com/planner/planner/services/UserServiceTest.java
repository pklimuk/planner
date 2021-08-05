package com.planner.planner.services;

import com.planner.planner.models.User;
import com.planner.planner.models.UserProfile;
import com.planner.planner.models.UserRole;
import com.planner.planner.models.registration.EmailValidator;
import com.planner.planner.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserProfileRepository userProfileRepository;
    @Mock private GroupRepository groupRepository;
    @Mock private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock private ConfirmationTokenService confirmationTokenService;
    @Mock private EmailValidator emailValidator;
    @Mock private DeadlineRepository deadlineRepository;
    @Mock private EventRepository eventRepository;
    private UserService underTest;

    @BeforeEach
    void setUp() throws IOException {
        underTest = new UserService(userRepository, userProfileRepository,
                groupRepository, bCryptPasswordEncoder,
                confirmationTokenService, emailValidator,
                deadlineRepository,  eventRepository);
    }

    @Test
    void canSignUpUser() {
        //given
        String firstName = "userFirstName";
        String lastName = "userLastName";
        String login = "userLogin";
        String password = "userPassword";
        String email = "userEmail@mail.com";
        LocalDate dob = LocalDate.of(2000, 5, 5);
        UserRole role = UserRole.USER;

        //when
        UserProfile userProfile = new UserProfile(firstName, lastName, email, dob);
        User user = new User(login, password, role, userProfile);
        underTest.signUpUser(firstName, lastName, email, dob, login, password, role);

        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser.getLogin()).isEqualTo(user.getLogin());

    }
}