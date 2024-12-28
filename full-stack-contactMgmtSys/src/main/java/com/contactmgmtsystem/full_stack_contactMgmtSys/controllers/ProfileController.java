package com.contactmgmtsystem.full_stack_contactMgmtSys.controllers;

import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;
import com.contactmgmtsystem.full_stack_contactMgmtSys.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.contactmgmtsystem.full_stack_contactMgmtSys.forms.UserForm;

@Controller
public class ProfileController {

    @Autowired
    private UserServices userServices;

    @RequestMapping("/home")
    public String home(Model model) {
        System.out.println("Home page handler");
        model.addAttribute("name", "Bhuwan");
        model.addAttribute("Youtube Channel", "CodesByBhuwan");
        return "home";
    }

    // About Me
    @RequestMapping("/about")
    public String aboutPage() {
        System.out.println("page loading");
        return "about";
    }

    // Service Page
    @RequestMapping("/services")
    public String servicesPage() {
        System.out.println("services page loading");
        return "services";
    }

    // Contact Page
    @RequestMapping("/contact")
    public String contactPage() {
        System.out.println("contact page loading");
        return new String("contact");
    }

    @RequestMapping("/login")
    public String login(){
        return new String ("fragment/in-up/login");
    }

    @RequestMapping("/register")
    public String register(Model model){

        UserForm userForm = new UserForm();
        /* To assign default value
        userForm.setName("MyName");
         */
        model.addAttribute("userForm", userForm);
        return new String ("fragment/in-up/register");
    }


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
    User user = User.builder()
            .name(userForm.getName())
            .email(userForm.getEmail())
            .password(userForm.getPassword())
            .about(userForm.getAbout())
            .phoneNumber(userForm.getPhoneNumber())
            .profilePic("https://t4.ftcdn.net/jpg/07/08/47/75/360_F_708477508_DNkzRIsNFgibgCJ6KoTgJjjRZNJD4mb4.jpg")
            .build();


        User savedUser = userServices.saveUser(user);
        System.out.println("user saved");
        return "redirect:/register";
    }
}




