package com.planner.planner.user;

import com.planner.planner.deadline.Deadline;
import com.planner.planner.event.Event;
import com.planner.planner.group.GroupRepository;
import com.planner.planner.userProfile.UserProfile;
import com.planner.planner.userProfile.UserProfileRepository;
import com.planner.planner.userProfile.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final GroupRepository groupRepository;


    @Autowired
    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository, UserProfileService userProfileService, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.groupRepository = groupRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void addNewUser(String firstName, String lastName, String email, LocalDate dob,
                           String login, String password) {
        Optional<User> userOptional =
                userRepository.findUserByLogin(login);
        Optional<UserProfile> userProfileOptional =
                userProfileRepository.findUserProfileByEmail(email);
        if (userProfileOptional.isPresent()){
            throw new IllegalStateException("This email has been already registered");
        }
        else if (userOptional.isPresent()){
            throw new IllegalStateException("The user with such login has been already registered");
        }
        else {
            UserProfile userProfile = new UserProfile(firstName, lastName, email, dob);
            User user = new User(login, password, userProfile);
            userRepository.save(user);
        }
    }

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
