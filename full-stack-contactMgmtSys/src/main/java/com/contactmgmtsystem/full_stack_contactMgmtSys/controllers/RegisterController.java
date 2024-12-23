package com.contactmgmtsystem.full_stack_contactMgmtSys.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.contactmgmtsystem.full_stack_contactMgmtSys.forms.UserForm;

@Controller
public class RegisterController {

 // Processing register
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
        System.out.println(userForm);
        return "redirect:/register";
    }
}
