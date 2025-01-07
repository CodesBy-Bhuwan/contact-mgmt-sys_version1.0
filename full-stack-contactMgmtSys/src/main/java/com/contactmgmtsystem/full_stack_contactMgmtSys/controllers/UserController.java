package com.contactmgmtsystem.full_stack_contactMgmtSys.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
        return "user/Dashboard";
    }


    @RequestMapping(value = "/profile", method = RequestMethod.GET)
//    In RequestMethod default method is GET
    public String userProfile(){
        System.out.println("This is userProfile");
        return "user/Profile";
    }
}
