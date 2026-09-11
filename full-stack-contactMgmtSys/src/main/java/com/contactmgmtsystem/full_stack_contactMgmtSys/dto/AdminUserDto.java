package com.contactmgmtsystem.full_stack_contactMgmtSys.dto;

import java.util.List;
import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;

public record AdminUserDto(
        String userId, String name, String email, String phoneNumber, String about,
        String profilePic, boolean enabled, boolean emailVerified,
        String provider, List<String> roles, long contactCount) {

    public static AdminUserDto from(User u, long contactCount) {
        return new AdminUserDto(u.getUserId(), u.getName(), u.getEmail(), u.getPhoneNumber(),
                u.getAbout(), u.getProfilePic(), u.isEnabled(), u.isEmailVerified(),
                u.getProviders().toString(), u.getRoleList(), contactCount);
    }
}