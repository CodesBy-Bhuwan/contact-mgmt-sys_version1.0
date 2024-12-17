package com.contactmgmtsystem.full_stack_contactMgmtSys.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

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
}
