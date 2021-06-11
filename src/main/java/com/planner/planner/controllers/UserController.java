package com.planner.planner.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.planner.planner.services.UserService;
import com.planner.planner.models.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDate;



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
        new_user_profile.setProfileImage(userProfile.getProfileImage());
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

    @PutMapping("userprofile/update_image")
    public void updateProfileImage(@RequestBody MultipartFile profile_image) throws IOException {
        userService.updateProfileImage(profile_image.getBytes());
    }

    @DeleteMapping
    public void deleteUser(){
        userService.deleteUser();
    }
}
