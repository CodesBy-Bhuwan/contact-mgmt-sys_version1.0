package com.contactmgmtsystem.full_stack_contactMgmtSys.controllers;


import com.contactmgmtsystem.full_stack_contactMgmtSys.helper.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    /*Steps to follow:
    *   User Dashboard
    *   User add contact page
    *   User view contact
    *   User edit contact
    *   User delete contact
    *   User search contact*/

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
//    In RequestMethod default method is GET
    public String userDashboard(){
        System.out.println("This is userDashboard");
        return "user/dashboard";
    }


    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
//    In RequestMethod default method is GET
    public String userProfile(Authentication authentication){

        String username =  Helper.getEmailOfLoggedInUser(authentication);
        logger.info( username + "User logged in");

        System.out.println("This is userProfile");
        return "user/profile";
    }

    @PostMapping("/authenticate")
    public String authenticateUser() {
        return "redirect:/user/dashboard";  // This might be handled internally by Spring Security
    }
}
