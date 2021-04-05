package com.planner.planner.userProfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public List<UserProfile> getUserProfiles() {
        return userProfileRepository.findAll();
    }

    public void addNewUserProfile(UserProfile userProfile) {
        Optional<UserProfile> userProfileOptional =
                userProfileRepository.findUserProfileByEmail(userProfile.getEmail());
        if (userProfileOptional.isPresent()){
            throw new IllegalStateException("This email has been already registered");
        }
        else {
            userProfileRepository.save(userProfile);
        }
    }

    public void deleteUserProfile(Long userProfileId) {
        boolean exists = userProfileRepository.existsById(userProfileId);
        if (!exists) {
            throw new IllegalStateException("User profile with Id " + userProfileId + " does not exists");
        }
        else {
            userProfileRepository.deleteById(userProfileId);
        }
    }

    @Transactional
    public void updateUserProfile(Long userProfileId, String firstName, String lastName,
                                  String email, LocalDate dob){
        UserProfile userProfile = userProfileRepository.findById(userProfileId).orElseThrow(() ->
                new IllegalStateException("User profile with Id " + userProfileId + " does not exists"));
        if (firstName != null && firstName.length() > 0 && !Objects.equals(userProfile.getFirstName(), firstName)) {
            userProfile.setFirstName(firstName);
        }
        if (lastName != null && lastName.length() > 0 && !Objects.equals(userProfile.getLastName(), lastName)) {
            userProfile.setLastName(lastName);
        }
        if (email != null && email.length() > 0 && !Objects.equals(userProfile.getEmail(), email)) {
            Optional<UserProfile> userProfileOptional =
                    userProfileRepository.findUserProfileByEmail(userProfile.getEmail());
            if (userProfileOptional.isPresent()){
                throw new IllegalStateException("This email has been already registered");
            }
            else {
                userProfile.setEmail(email);
            }
        }
        if (dob != null && !Objects.equals(userProfile.getDob(), dob)) {
            userProfile.setDob(dob);
        }
        }
}
