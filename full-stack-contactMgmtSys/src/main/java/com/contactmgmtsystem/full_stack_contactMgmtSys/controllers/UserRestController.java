package com.contactmgmtsystem.full_stack_contactMgmtSys.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import com.contactmgmtsystem.full_stack_contactMgmtSys.dto.UserDto;
import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;
import com.contactmgmtsystem.full_stack_contactMgmtSys.services.UserServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired private UserServices userServices;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public record ProfileUpdate(String name, String phoneNumber, String about) {}

    @PutMapping("/me")
    public ResponseEntity<?> updateMe(@RequestBody ProfileUpdate body, Authentication auth) {
        User user = userServices.getUserByEmail(auth.getName());
        // Email deliberately NOT editable here — it's the login identity
        if (body.name() != null) user.setName(body.name());
        if (body.phoneNumber() != null) user.setPhoneNumber(body.phoneNumber());
        if (body.about() != null) user.setAbout(body.about());
        return ResponseEntity.ok(UserDto.from(userServices.updateUser(user).orElse(user)));
    }

    @PostMapping("/me/picture")
    public ResponseEntity<?> uploadPicture(@RequestParam("file") MultipartFile file, Authentication auth) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
        }
        String type = file.getContentType();
        if (type == null || !type.startsWith("image/")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Only image files are allowed"));
        }

        try {
            String ext = switch (type) {
                case "image/png" -> "png";
                case "image/gif" -> "gif";
                case "image/webp" -> "webp";
                default -> "jpg";
            };
            String filename = UUID.randomUUID() + "." + ext;
            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);
            file.transferTo(dir.resolve(filename).toAbsolutePath());

            User user = userServices.getUserByEmail(auth.getName());
            user.setProfilePic("/uploads/" + filename);   // relative URL, served by WebConfig
            return ResponseEntity.ok(UserDto.from(userServices.updateUser(user).orElse(user)));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Upload failed"));
        }
    }
}