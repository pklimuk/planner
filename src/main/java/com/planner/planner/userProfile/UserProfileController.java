package com.planner.planner.userProfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(path = "api/userprofile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    // TODO: Delete test_func after tests
    @GetMapping("/test")
    public String test_func() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }
        else{
            return "There is no logged in user";
        }
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
