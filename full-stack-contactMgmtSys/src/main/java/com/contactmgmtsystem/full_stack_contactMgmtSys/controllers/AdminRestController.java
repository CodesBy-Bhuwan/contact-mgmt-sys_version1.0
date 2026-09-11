package com.contactmgmtsystem.full_stack_contactMgmtSys.controllers;

import java.util.List;
import java.util.Map;

import com.contactmgmtsystem.full_stack_contactMgmtSys.dto.AdminUserDto;
import com.contactmgmtsystem.full_stack_contactMgmtSys.dto.ContactDto;
import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;
import com.contactmgmtsystem.full_stack_contactMgmtSys.helper.ResourceNotFoundException;
import com.contactmgmtsystem.full_stack_contactMgmtSys.services.ContactService;
import com.contactmgmtsystem.full_stack_contactMgmtSys.services.UserServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired private UserServices userServices;
    @Autowired private ContactService contactService;

    public record AdminUserUpdate(String name, String email, String phoneNumber, String about) {}
    public record EnabledRequest(boolean enabled) {}
    public record PasswordRequest(String password) {}
    public record AdminUserDetail(AdminUserDto user, List<ContactDto> contacts) {}

    // ---- See / view every user (requirement 3: no ownership checks here) ----

    @GetMapping("/users")
    public List<AdminUserDto> allUsers() {
        return userServices.getAllUsers().stream()
                .map(u -> AdminUserDto.from(u, contactService.countByUserId(u.getUserId())))
                .toList();
    }

    @GetMapping("/users/{id}")
    public AdminUserDetail oneUser(@PathVariable String id) {
        User u = userServices.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new AdminUserDetail(
                AdminUserDto.from(u, contactService.countByUserId(id)),
                contactService.getAllByUserId(id));   // sees anyone's contacts without their permission
    }

    // ---- Edit any user ----

    @PutMapping("/users/{id}")
    public ResponseEntity<?> editUser(@PathVariable String id, @RequestBody AdminUserUpdate body) {
        // Fetch the FULL entity, change only provided fields — never pass a partial User
        // into updateUser(), which copies every field.
        User u = userServices.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (body.name() != null) u.setName(body.name());
        if (body.email() != null) u.setEmail(body.email());
        if (body.phoneNumber() != null) u.setPhoneNumber(body.phoneNumber());
        if (body.about() != null) u.setAbout(body.about());
        return ResponseEntity.ok(AdminUserDto.from(
                userServices.updateUser(u).orElse(u), contactService.countByUserId(id)));
    }

    @PutMapping("/users/{id}/password")
    public ResponseEntity<?> resetPassword(@PathVariable String id, @RequestBody PasswordRequest body) {
        if (body.password() == null || body.password().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Password is required"));
        }
        userServices.updatePassword(id, body.password());
        return ResponseEntity.ok(Map.of("message", "Password reset"));
    }

    // ---- "Delete" = soft: hidden from login, data preserved ----

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> softDeleteUser(@PathVariable String id) {
        userServices.setUserEnabled(id, false);
        return ResponseEntity.ok(Map.of("message", "User disabled (soft-deleted). Data preserved."));
    }

    @PutMapping("/users/{id}/enabled")
    public ResponseEntity<?> setEnabled(@PathVariable String id, @RequestBody EnabledRequest body) {
        userServices.setUserEnabled(id, body.enabled());
        return ResponseEntity.ok(Map.of("message", body.enabled() ? "User restored" : "User disabled"));
    }
}