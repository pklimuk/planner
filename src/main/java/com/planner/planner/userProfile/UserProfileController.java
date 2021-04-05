package com.planner.planner.userProfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/userprofile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public List<UserProfile> getUserProfiles() {
        return userProfileService.getUserProfiles();
    }

    @PostMapping
    public void addNewUserProfile(@RequestBody UserProfile userProfile) {
        userProfileService.addNewUserProfile(userProfile);
    }

    @DeleteMapping(path = "{userProfileId}")
    public void deleteUserProfile(@PathVariable("userProfileId") Long userProfileId) {
        userProfileService.deleteUserProfile(userProfileId);
    }

    @PutMapping(path = "{userProfileId}")
    public void updateUerProfile(@PathVariable("userProfileId") Long userProfileId,
                                 @RequestParam(required = false) String firstName,
                                 @RequestParam(required = false) String lastName,
                                 @RequestParam(required = false) String email,
                                 @RequestParam(required = false) LocalDate dob) {
        userProfileService.updateUserProfile(userProfileId, firstName, lastName, email, dob);
    }
}
