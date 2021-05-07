package com.planner.planner.user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.planner.planner.userProfile.UserProfile;
import com.planner.planner.userProfile.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(path = "api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


//    @GetMapping
//    public User simple_func(){
//        String user_login = userService.getLoggedUserUserName();
//        return userService.getUserByUsername(user_login);
//    }
}
