package com.contactmgmtsystem.full_stack_contactMgmtSys.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Contact {

    @Id
    private String id;
    @Column(name = "user_name", nullable=false)
    private String name;
    @Column(unique=true, nullable=false)
    private String email;
    private String phoneNumber;
    private String address;
    @Column(length = 1000)
    private String description;
    private String picture;
    private boolean fav=false;

    // If any link 
    private String webLink;
    private String facebookLink;
    // private List<String> socialLinks = new ArrayList<>();

    @ManyToOne
    private User user;

//    Mapping contact with socialLink
    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<SocialLink> socialLinks = new ArrayList<>();
}
