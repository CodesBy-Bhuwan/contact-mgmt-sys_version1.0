package com.contactmgmtsystem.full_stack_contactMgmtSys.controllers;

import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;
import com.contactmgmtsystem.full_stack_contactMgmtSys.forms.UserForm;
import com.contactmgmtsystem.full_stack_contactMgmtSys.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//import com.contactmgmtsystem.full_stack_contactMgmtSys.forms.UserForm;

@Controller
public class RegisterController {

    @Autowired
    private UserServices userServices;

//  Processing register
    @RequestMapping(value="/do-register", method = RequestMethod.POST)
    public String processRegister(@ModelAttribute UserForm userForm){
        System.out.println("procseeing registraiton");
/*  Steps to follow for the registration:
 1). Fetch the data or Input from the form
 2). Validate the input data
 3). Save into the database
 4). Message if needed
 5). Redirect to login page
*/
        /*
    User user = User.builder()
            .name(userForm.getName())
            .email(userForm.getEmail())
            .password(userForm.getPassword())
            .about(userForm.getAbout())
            .phoneNumber(userForm.getPhoneNumber())
            .profilePic("https://t4.ftcdn.net/jpg/07/08/47/75/360_F_708477508_DNkzRIsNFgibgCJ6KoTgJjjRZNJD4mb4.jpg")
            .build(); */

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setAbout(userForm.getAbout());
//        user.setEnabled(true);
        user.setProfilePic("https://t4.ftcdn.net/jpg/07/08/47/75/360_F_708477508_DNkzRIsNFgibgCJ6KoTgJjjRZNJD4mb4.jpg");


        User saveUser = userServices.saveUser(user);
//        System.out.println(userForm);   tested
        System.out.println("saved user");
        return "redirect:/register";
    }
}
