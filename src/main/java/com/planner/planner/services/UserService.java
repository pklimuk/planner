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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG =
            "user with login %s not found";

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final GroupRepository groupRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailValidator emailValidator;
    private final DeadlineRepository deadlineRepository;
    private final EventRepository eventRepository;
    private final Integer minimal_user_age = 6;


    public byte[] getDefaultProfileImage() throws IOException {
        String path = new File("src/main/resources/images/default-user-image.jpg").getAbsolutePath();
        BufferedImage bImage = ImageIO.read(new File(path));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos );
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

    public Boolean userAgeIsCorrect(LocalDate dob){
        boolean user_age_is_correct = false;
        if (ChronoUnit.YEARS.between(dob, LocalDate.now()) > minimal_user_age){
            user_age_is_correct = true;
        }
        return user_age_is_correct;
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
        if (!userAgeIsCorrect(dob)){
            throw new IllegalStateException("You must be at least " + minimal_user_age + " years old to use this app");
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
            String currentUserName = authentication.getName();
            return currentUserName;
        }
        else{
            return "There is no logged in user";
        }
    }

    public User getUserByUsername(String login){
        Optional<User> userOptional = userRepository.findUserByLogin(login);
        return userRepository.getOne(userOptional.get().getId());
    }

    public User getCurrentUser(){
        return getUserByUsername(getLoggedUserUserName());
    }

    public UserProfile getUserProfile() {
        User user = getCurrentUser();
        return user.getUser_profile();
    }

    @Transactional
    public void updateUserProfile(String new_firstName, String new_lastName,
                                  String new_email, LocalDate new_dob){
        User user = getCurrentUser();
        UserProfile userProfile = userProfileRepository.findById(user.getId()).orElseThrow(() ->
                new IllegalStateException("User profile with email " +
                        user.getUser_profile().getEmail() + " does not exists"));
        if (new_firstName != null && new_firstName.length() > 0 &&
                !Objects.equals(userProfile.getFirstName(), new_firstName)) {
            userProfile.setFirstName(new_firstName);
        }
        if (new_lastName != null && new_lastName.length() > 0 &&
                !Objects.equals(userProfile.getLastName(), new_lastName)) {
            userProfile.setLastName(new_lastName);
        }
        if (new_email != null && new_email.length() > 0 && !Objects.equals(userProfile.getEmail(), new_email)) {
            Optional<UserProfile> userProfileOptional =
                    userProfileRepository.findUserProfileByEmail(new_email);
            if (userProfileOptional.isPresent()){
                throw new IllegalStateException("This email has been already registered");
            }
            else if (emailValidator.test(new_email)){
                userProfile.setEmail(new_email);
            }
        }
        if (!userAgeIsCorrect(new_dob)){
            throw new IllegalStateException("You must be at least " + minimal_user_age + " years old to use this app");
        }
        else if (new_dob != null && !Objects.equals(userProfile.getDob(), new_dob)) {
            userProfile.setDob(new_dob);
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

    public void updateProfileImage(byte[] image){
        User user = getCurrentUser();
        user.getUser_profile().setProfileImage(image);
        userRepository.save(user);
    }
}
