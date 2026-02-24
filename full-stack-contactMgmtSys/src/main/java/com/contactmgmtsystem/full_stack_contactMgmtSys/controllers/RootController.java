package com.contactmgmtsystem.full_stack_contactMgmtSys.controllers;

import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;
import com.contactmgmtsystem.full_stack_contactMgmtSys.helper.Helper;
import com.contactmgmtsystem.full_stack_contactMgmtSys.services.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class RootController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserServices userServices;

    @ModelAttribute
    public void addLoggedInUserInfo(Model model, Authentication authentication) {

//      If the user is not authenticated then user willn't be able to view profile or dashboard
        if(authentication == null) {
            return;
        }

        System.out.println("Testing if all model(inside user's profile) gets user's info");

        String username =  Helper.getEmailOfLoggedInUser(authentication);
        logger.info( username + "User logged in");


//       Fetch user's data from database
        User user = userServices.getUserByEmail(username);
        System.out.println(user);
        System.out.println(user.getUsername());
        System.out.println(user.getEmail());
        model.addAttribute("loggedInUser", user);
/*
        User user = userRepository.findByUsername(principal.getName());
        if (user == null) {
            return;
        }

        model.addAttribute("username", user.getUsername());
*/

    }

}