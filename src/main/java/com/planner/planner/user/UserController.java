package com.planner.planner.user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.planner.planner.userProfile.UserProfile;
import com.planner.planner.userProfile.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

//    @PostMapping
//    public void addNewUser(@RequestBody User user,@RequestParam(required = false) Long userProfileId) {
//        userService.addNewUser(user, userProfileId);
//    }
    @PostMapping
    public void addNewUser(@RequestBody ObjectNode objectNode) {
        String firstName = objectNode.get("firstName").asText();
        String lastName = objectNode.get("lastName").asText();
        String email = objectNode.get("email").asText();
        LocalDate dob = LocalDate.parse(objectNode.get("dob").asText());
        String login = objectNode.get("login").asText();
        String password = objectNode.get("password").asText();
        UserRole userRole = UserRole.USER;
        userService.addNewUser(firstName, lastName, email, dob, login, password, userRole);
    }
}
