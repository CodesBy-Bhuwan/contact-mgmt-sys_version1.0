package com.contactmgmtsystem.full_stack_contactMgmtSys.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "user")
@Table(name = "user")
// Lombok featur
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    // Only basic information will be asked;

    @Id
    private String userId;
    @Column(name = "user_name", nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String phoneNumber;
    @Column(length = 1000)
    private String about;
    @Column(length = 1000)
    private String profilePic;
    private String password;
    
    // Verification information incase available;
    private boolean enabled = false;
    private boolean emailVerified = false;
    private boolean phoneNumberVerified = false;

    /* If user signed/logged from other sources 
    like: GOOGLE, LINKEDIN, FACEBOOK, GITHUB... */ 
    private  Providers providers=Providers.SELF;
    private String providerUserId;

}
