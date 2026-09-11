package com.contactmgmtsystem.full_stack_contactMgmtSys.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contacts",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "email"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact {

    @Id
    private String id;   // String UUID, generated in the service — matches frontend types

    @Column(name = "user_name", nullable = false)
    private String name;

    // The contact's login/username for whatever service this entry is for
    private String username;

    @Column(nullable = false)
    private String email;

    private String phoneNumber;
    private String address;

    @Column(length = 1000)
    private String description;

    private String picture;

    @Builder.Default
    private boolean fav = false;

    private String webLink;
    private String facebookLink;

    // AES-GCM, Base64(iv+ciphertext). NEVER returned by list/get endpoints.
    private String encryptedPassword;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Builder.Default
    private List<SocialLink> socialLinks = new ArrayList<>();
}