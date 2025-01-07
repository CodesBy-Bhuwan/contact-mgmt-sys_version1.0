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
        return "user/Dashboard";
    }
}
