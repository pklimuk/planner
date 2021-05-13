package com.planner.planner.user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.planner.planner.userProfile.UserProfile;
import com.planner.planner.userProfile.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping(path = "api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/userprofile")
    public UserProfile getUserProfile(){
        UserProfile userProfile = userService.getUserProfile();
        UserProfile new_user_profile = new UserProfile(userProfile.getFirstName(),
                userProfile.getLastName(), userProfile.getEmail(), userProfile.getDob());
        return new_user_profile;
    }

    @PutMapping("/userprofile")
    public void updateUserProfile(@RequestBody ObjectNode objectNode){
        String new_firstName = null;
        String new_lastName = null;
        String new_email = null;
        LocalDate new_dob = null;
        if (objectNode.get("new_firstName") != null){
            new_firstName = objectNode.get("new_firstName").asText();
        }
        if (objectNode.get("new_lastName") != null){
            new_lastName = objectNode.get("new_lastName").asText();
        }
        if (objectNode.get("new_email") != null){
            new_email = objectNode.get("new_email").asText();
        }
        if (objectNode.get("new_dob") != null){
            new_dob = LocalDate.parse(objectNode.get("new_dob").asText());
        }
        userService.updateUserProfile(new_firstName, new_lastName, new_email, new_dob);
    }

//    @GetMapping
//    public User simple_func(){
//        String user_login = userService.getLoggedUserUserName();
//        return userService.getUserByUsername(user_login);
//    }
}
