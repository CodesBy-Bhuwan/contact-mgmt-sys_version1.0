package com.contactmgmtsystem.full_stack_contactMgmtSys.controllers;

import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;
import com.contactmgmtsystem.full_stack_contactMgmtSys.forms.UserForm;
import com.contactmgmtsystem.full_stack_contactMgmtSys.message.Message;
import com.contactmgmtsystem.full_stack_contactMgmtSys.message.MessageType;
import com.contactmgmtsystem.full_stack_contactMgmtSys.services.UserServices;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    private Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private UserServices userServices;

    @RequestMapping(value = "/do-register", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm,
                                  BindingResult rBindingResult,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {

        // 1) Validation errors -> redirect back, carrying the typed values + errors
        if (rBindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userForm", userForm);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.userForm", rBindingResult);
            return "redirect:/register";
        }

        // 2) Duplicate email -> friendly message instead of a raw 500
        if (userServices.isUserExistByEmail(userForm.getEmail())) {
            session.setAttribute("message", Message.builder()
                    .content("Email is already registered. Try logging in instead.")
                    .type(MessageType.red)
                    .build());
            return "redirect:/register";
        }

        // 3) Build entity
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setAbout(userForm.getAbout());
        user.setProfilePic("https://t4.ftcdn.net/jpg/07/08/47/75/360_F_708477508_DNkzRIsNFgibgCJ6KoTgJjjRZNJD4mb4.jpg");

        // 4) Save, with a backstop for unique-constraint violations (e.g. duplicate phone)
        try {
            userServices.saveUser(user);
        } catch (DataIntegrityViolationException e) {
            logger.error("Registration failed for {}", userForm.getEmail(), e);
            session.setAttribute("message", Message.builder()
                    .content("Registration failed — email or phone number may already be in use.")
                    .type(MessageType.red)
                    .build());
            return "redirect:/register";
        }

        session.setAttribute("message", Message.builder()
                .content("Registered successfully")
                .type(MessageType.green)
                .build());

        return "redirect:/login";
    }
}