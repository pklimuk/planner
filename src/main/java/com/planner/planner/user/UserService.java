package com.planner.planner.user;

import com.planner.planner.group.GroupRepository;
import com.planner.planner.registration.EmailValidator;
import com.planner.planner.registration.token.ConfirmationToken;
import com.planner.planner.registration.token.ConfirmationTokenService;
import com.planner.planner.userProfile.UserProfile;
import com.planner.planner.userProfile.UserProfileRepository;
import com.planner.planner.userProfile.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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


    @Autowired
    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository, UserProfileService userProfileService, GroupRepository groupRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ConfirmationTokenService confirmationTokenService, EmailValidator emailValidator) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.groupRepository = groupRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.confirmationTokenService = confirmationTokenService;
        this.emailValidator = emailValidator;
    }

    @Override
    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException {
        return userRepository.findUserByLogin(login)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, login)));
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
        } else {
            String encodedPassword = bCryptPasswordEncoder
                    .encode(password);
            UserProfile userProfile = new UserProfile(firstName, lastName, email, dob);
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

//        TODO: SEND EMAIL

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
        if (new_dob != null && !Objects.equals(userProfile.getDob(), new_dob)) {
            userProfile.setDob(new_dob);
        }
    }

    // TODO: Implement Deletion

//    public void deleteUser(Long userId) {
//        boolean exists = userRepository.existsById(userId);
//        if (!exists) {
//            throw new IllegalStateException("User with Id " + userId + " does not exists");
//        }
//        else {
////            User user = userRepository.getOne(userId);
////            List<Event> user_events = user.getEvents();
////            List<Deadline> user_deadlines = user.getDeadlines();
//
//            userRepository.deleteById(userId);
//        }
//    }


}
