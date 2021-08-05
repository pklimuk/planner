package com.planner.planner.services;

import com.planner.planner.models.User;
import com.planner.planner.models.UserRole;
import com.planner.planner.repositories.DeadlineRepository;
import com.planner.planner.repositories.EventRepository;
import com.planner.planner.repositories.GroupRepository;
import com.planner.planner.models.registration.EmailValidator;
import com.planner.planner.models.registration.ConfirmationToken;
import com.planner.planner.repositories.UserRepository;
import com.planner.planner.models.UserProfile;
import com.planner.planner.repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG =
            "user with login %s not found";
    private final static int MINIMAL_USER_AGE = 6;

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final GroupRepository groupRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailValidator emailValidator;
    private final DeadlineRepository deadlineRepository;
    private final EventRepository eventRepository;

    public byte[] getDefaultProfileImage() throws IOException {
        URL fileURL = ResourceLoader.class.getResource("/images/default-user-image.jpg");
        BufferedImage bImage = ImageIO.read(fileURL.openStream());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos);
        return bos.toByteArray();
    }

    private final byte[] defaultProfileImage = getDefaultProfileImage();

    @Autowired
    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository,
                       GroupRepository groupRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                       ConfirmationTokenService confirmationTokenService, EmailValidator emailValidator,
                       DeadlineRepository deadlineRepository, EventRepository eventRepository) throws IOException {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.groupRepository = groupRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.confirmationTokenService = confirmationTokenService;
        this.emailValidator = emailValidator;
        this.deadlineRepository = deadlineRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException {
        return userRepository.findUserByLogin(login)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, login)));
    }

    public Boolean userAgeIsCorrect(LocalDate dob) {
        boolean userAgeIsCorrect = false;
        if (ChronoUnit.YEARS.between(dob, LocalDate.now()) > MINIMAL_USER_AGE) {
            userAgeIsCorrect = true;
        }
        return userAgeIsCorrect;
    }


    public String signUpUser(String firstName, String lastName, String email, LocalDate dob,
                             String login, String password, UserRole userRole) {

        Optional<User> userOptional =
                userRepository.findUserByLogin(login);
        Optional<UserProfile> userProfileOptional =
                userProfileRepository.findUserProfileByEmail(email);
        if (userProfileOptional.isPresent()) {
            throw new IllegalStateException("This email has been already registered");
        } else if (userOptional.isPresent()) {
            throw new IllegalStateException("The user with such login has been already registered");
        }
        if (!userAgeIsCorrect(dob)) {
            throw new IllegalStateException("You must be at least " + MINIMAL_USER_AGE + " years old to use this app");
        }
        else {
            String encodedPassword = bCryptPasswordEncoder
                    .encode(password);
            UserProfile userProfile = new UserProfile(firstName, lastName, email, dob);
            userProfile.setProfileImage(defaultProfileImage);
            User user = new User(login, encodedPassword, userRole, userProfile);
            userRepository.save(user);

            String token = UUID.randomUUID().toString();

            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    user);

            confirmationTokenService.saveConfirmationToken(
                    confirmationToken);
            return token;
        }
    }

    public int enableUser(String login) {
        return userRepository.enableUser(login);
    }

    public String getLoggedUserUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        else {
            return "There is no logged in user";
        }
    }

    public User getUserByUsername(String login) {
        Optional<User> userOptional = userRepository.findUserByLogin(login);
        return userRepository.getOne(userOptional.get().getId());
    }

    public User getCurrentUser() {
        return getUserByUsername(getLoggedUserUserName());
    }

    public UserProfile getUserProfile() {
        User user = getCurrentUser();
        return user.getUser_profile();
    }

    @Transactional
    public void updateUserProfile(String newFirstName, String newLastName,
                                  String newEmail, LocalDate newDob) {
        User user = getCurrentUser();
        UserProfile userProfile = userProfileRepository.findById(user.getId()).orElseThrow(() ->
                new IllegalStateException("User profile with email " +
                        user.getUser_profile().getEmail() + " does not exists"));
        if (newFirstName != null && newFirstName.length() > 0
                && !Objects.equals(userProfile.getFirstName(), newFirstName)) {
            userProfile.setFirstName(newFirstName);
        }
        if (newLastName != null && newLastName.length() > 0
                && !Objects.equals(userProfile.getLastName(), newLastName)) {
            userProfile.setLastName(newLastName);
        }
        if (newEmail != null && newEmail.length() > 0 && !Objects.equals(userProfile.getEmail(), newEmail)) {
            Optional<UserProfile> userProfileOptional =
                    userProfileRepository.findUserProfileByEmail(newEmail);
            if (userProfileOptional.isPresent()) {
                throw new IllegalStateException("This email has been already registered");
            }
            else if (emailValidator.test(newEmail)) {
                userProfile.setEmail(newEmail);
            }
        }
        if (!userAgeIsCorrect(newDob)) {
            throw new IllegalStateException("You must be at least " + MINIMAL_USER_AGE + " years old to use this app");
        }
        else if (newDob != null && !Objects.equals(userProfile.getDob(), newDob)) {
            userProfile.setDob(newDob);
        }
    }

    public void deleteUser() {
        User user = getCurrentUser();
        for (var deadline: user.getDeadlines()) {
            deadlineRepository.deleteDeadlineById(deadline.getId());
        }
        for (var event: user.getEvents()) {
            eventRepository.deleteEventById(event.getId());
        }
        for (var group: user.getGroups()) {
            groupRepository.deleteById(group.getId());
        }
        userProfileRepository.deleteById(user.getUser_profile().getId());
    }

    public void updateProfileImage(byte[] image) {
        User user = getCurrentUser();
        user.getUser_profile().setProfileImage(image);
        userRepository.save(user);
    }
}
