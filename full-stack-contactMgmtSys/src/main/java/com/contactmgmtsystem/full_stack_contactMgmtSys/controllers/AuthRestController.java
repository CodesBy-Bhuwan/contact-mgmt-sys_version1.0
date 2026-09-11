package com.contactmgmtsystem.full_stack_contactMgmtSys.controllers;

import com.contactmgmtsystem.full_stack_contactMgmtSys.dto.UserDto;
import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;
import com.contactmgmtsystem.full_stack_contactMgmtSys.forms.UserForm;
import com.contactmgmtsystem.full_stack_contactMgmtSys.helper.Helper;
import com.contactmgmtsystem.full_stack_contactMgmtSys.helper.ResourceNotFoundException;
import com.contactmgmtsystem.full_stack_contactMgmtSys.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    @Autowired
    private UserServices userServices;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserForm form, BindingResult br) {

        // Validation errors -> 400 with {field: message} so React can show inline errors
        if (br.hasErrors()) {
            Map<String, String> errors = br.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            FieldError::getDefaultMessage,
                            (a, b) -> a));
            return ResponseEntity.badRequest().body(errors);
        }

        // Duplicate email -> 400 keyed to the field, same shape as above
        if (userServices.isUserExistByEmail(form.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("email", "Email is already registered"));
        }

        User user = new User();
        user.setName(form.getName());
        user.setEmail(form.getEmail());
        user.setPassword(form.getPassword());
        user.setPhoneNumber(form.getPhoneNumber());
        user.setAbout(form.getAbout());
        user.setProfilePic("https://t4.ftcdn.net/jpg/07/08/47/75/360_F_708477508_DNkzRIsNFgibgCJ6KoTgJjjRZNJD4mb4.jpg");

        User saved = userServices.saveUser(user);   // encodes password, generates UUID, assigns role
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDto.from(saved));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        String email = Helper.getEmailOfLoggedInUser(authentication);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            User user = userServices.getUserByEmail(email);   // fetch FIRST — user exists from here on
            if (!user.isEnabled()) {                          // THEN check
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Account disabled"));
            }
            return ResponseEntity.ok(UserDto.from(user));
        } catch (ResourceNotFoundException e) {
            // Authenticated session but no DB row (e.g. user deleted) -> treat as logged out
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}