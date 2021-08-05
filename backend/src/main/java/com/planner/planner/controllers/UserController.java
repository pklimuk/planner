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
    public UserProfile getUserProfile() {
        UserProfile userProfile = userService.getUserProfile();
        UserProfile newUserProfile = new UserProfile(userProfile.getFirstName(),
                userProfile.getLastName(), userProfile.getEmail(), userProfile.getDob());
        newUserProfile.setProfileImage(userProfile.getProfileImage());
        return newUserProfile;
    }

    @PutMapping("/userprofile")
    public void updateUserProfile(@RequestBody ObjectNode objectNode) {
        String newFirstName = null;
        String newLastName = null;
        String newEmail = null;
        LocalDate newDob = null;
        if (objectNode.get("new_firstName") != null) {
            newFirstName = objectNode.get("new_firstName").asText();
        }
        if (objectNode.get("new_lastName") != null) {
            newLastName = objectNode.get("new_lastName").asText();
        }
        if (objectNode.get("new_email") != null) {
            newEmail = objectNode.get("new_email").asText();
        }
        if (objectNode.get("new_dob") != null) {
            newDob = LocalDate.parse(objectNode.get("new_dob").asText());
        }
        userService.updateUserProfile(newFirstName, newLastName, newEmail, newDob);
    }

    @PutMapping("userprofile/update_image")
    public void updateProfileImage(@RequestBody MultipartFile profile_image) throws IOException {
        userService.updateProfileImage(profile_image.getBytes());
    }

    @DeleteMapping
    public void deleteUser() {
        userService.deleteUser();
    }
}
