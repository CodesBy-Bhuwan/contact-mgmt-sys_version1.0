package com.contactmgmtsystem.full_stack_contactMgmtSys.controllers;

import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;
import com.contactmgmtsystem.full_stack_contactMgmtSys.helper.Helper;
import com.contactmgmtsystem.full_stack_contactMgmtSys.helper.ResourceNotFoundException;
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

        if (authentication == null || !authentication.isAuthenticated()) {
            return;
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);
        if (username == null) {
            return;
        }

        try {
            User user = userServices.getUserByEmail(username);
            model.addAttribute("loggedInUser", user);
        } catch (ResourceNotFoundException e) {
            // Authenticated principal missing from DB (e.g. deleted mid-session).
            // Render pages without the attribute instead of 500-ing on every request.
            logger.warn("Logged-in user not found in DB: {}", username);
        }
    }
}