package com.contactmgmtsystem.full_stack_contactMgmtSys.dto;

import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.Contact;

public record ContactDto(
        String id, String name, String username, String email,
        String phoneNumber, String address, String description,
        String webLink, String facebookLink,
        boolean fav, boolean hasPassword) {

    public static ContactDto from(Contact c) {
        return new ContactDto(c.getId(), c.getName(), c.getUsername(), c.getEmail(),
                c.getPhoneNumber(), c.getAddress(), c.getDescription(),
                c.getWebLink(), c.getFacebookLink(), c.isFav(),
                c.getEncryptedPassword() != null);
    }
}