package com.contactmgmtsystem.full_stack_contactMgmtSys.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProfileController {

    @RequestMapping("/home")
    public String home(Model model){
        System.out.println("Home page handler");
        model.addAttribute("name", "Bhuwan");
        model.addAttribute("Youtube Channel", "CodesByBhuwan");
        return "home";
    }

    // About Me
    @RequestMapping("/about")
    public String aboutPage(){
        System.out.println("page loading");
        return "about";
    }

    // Service Page
    @RequestMapping("/services")
    public String servicesPage(){
        System.out.println("services page loading");
        return "services";
    }
}
