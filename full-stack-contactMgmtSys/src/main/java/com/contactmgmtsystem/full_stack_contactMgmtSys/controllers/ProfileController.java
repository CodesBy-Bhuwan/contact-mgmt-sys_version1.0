package com.contactmgmtsystem.full_stack_contactMgmtSys.controllers;

import com.contactmgmtsystem.full_stack_contactMgmtSys.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

   

}
