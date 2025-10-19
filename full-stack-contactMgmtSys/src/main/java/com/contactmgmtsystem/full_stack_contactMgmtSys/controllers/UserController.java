package com.contactmgmtsystem.full_stack_contactMgmtSys.controllers;


import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;
import com.contactmgmtsystem.full_stack_contactMgmtSys.helper.Helper;
import com.contactmgmtsystem.full_stack_contactMgmtSys.services.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserServices userServices;




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



    @RequestMapping(value = "/profile", method = RequestMethod.GET)
//    In RequestMethod default method is GET
    public String userProfile(Model model, Authentication authentication){



        return "user/profile";
    }

    @PostMapping("/authenticate")
    public String authenticateUser() {
        return "redirect:/user/dashboard";  // This might be handled internally by Spring Security
    }
}
