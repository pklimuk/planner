package com.planner.planner.security.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
@RequestMapping("/")
public class LoginController {

    @GetMapping("login")
    public String login(){
        return "login";
    }

//    @GetMapping("http://localhost:3000/users")
//    public String getusers(){
//        return "users";
//    }
}
