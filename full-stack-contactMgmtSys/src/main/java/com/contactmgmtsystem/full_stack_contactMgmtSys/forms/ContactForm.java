package com.contactmgmtsystem.full_stack_contactMgmtSys.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ContactForm {

    @NotBlank(message = "Name is required")
    private String name;

    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;

    @Size(max = 14, message = "Invalid phone number")
    private String phoneNumber;

    private String address;

    @Size(max = 1000)
    private String description;

    private String webLink;
    private String facebookLink;

    // Optional. On create: stored encrypted if provided.
    // On update: blank = keep existing password (never blanked out by accident).
    private String password;
}