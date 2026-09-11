package com.contactmgmtsystem.full_stack_contactMgmtSys.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // FIX: was no generation -> every insert fails
    private Long id;

    private String link;
    private String title;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contact contact;
}