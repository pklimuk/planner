package com.planner.planner.registration;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class EmailValidator implements Predicate<String> {

    @Override
    public boolean test(String email) {
        Boolean email_is_valid = false;
        if (email.contains("@")){
            email_is_valid = true;
        }
        else{
            throw new IllegalStateException("This email is invalid");
        }
        return email_is_valid;
//        TODO: Implement better email validation
    }
}
