package com.contactmgmtsystem.full_stack_contactMgmtSys.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity(name = "user")
@Table(name = "user")
// Lombok feature
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {
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
    private boolean enabled = true;
    private boolean emailVerified = false;
    private boolean phoneNumberVerified = false;

    /* If user signed/logged from other sources 
    like: GOOGLE, LINKEDIN, FACEBOOK, GITHUB... */
    @Enumerated(value = EnumType.STRING)
    private  Providers providers=Providers.SELF;
    private String providerUserId;


//     Mapping technique used with contact <=> user
    /*
    cascade: if user deletes/updates contact will also get deleted/updated
    fetch: we fetched user and until we need contact we won't use db query
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        /* listed the roles[ADMIN, USER or other] and colletion of SimpleGrantedAuthority{roles(admin,user,other)}*/
        Collection<SimpleGrantedAuthority> roles= roleList
                .stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors
                        .toSet());

        return roles;
    }

    @Override
//    Here we want to make user to give username as email while loggin' in not username else we can also phoneNumber or username itself as required
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
//        we can make it dynamically too but for now just to test
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

//    this is getter given by UserDetails itself but incase we have already enrolled in above defination section but also need to return dynamically or not assigned in above
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public String getPassword() {
        return this.password;
    }
}
