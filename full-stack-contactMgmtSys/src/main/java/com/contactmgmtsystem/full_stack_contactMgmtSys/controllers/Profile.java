package com.contactmgmtsystem.full_stack_contactMgmtSys.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Profile {

    @RequestMapping("/home")
    public String home(Model model){
        System.out.println("Home page handler");
        model.addAttribute("name", "Bhuwan");
        model.addAttribute("Youtube Channel", "BashyalBhuwan");

        return "home";

    }
}
