package com.contactmgmtsystem.full_stack_contactMgmtSys.dto;

import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.Providers;
import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;

import java.util.List;

public record UserDto(
            String userId,
            String name,
            String email,
            String phoneNumber,
            String about,
            String profilePic,
            boolean emailVerified,
            Providers provider,
            List<String> role
    ) {
        public static UserDto from(User u) {
            return new UserDto(
                    u.getUserId(), u.getName(), u.getEmail(), u.getPhoneNumber(),
                    u.getAbout(), u.getProfilePic(), u.isEmailVerified(), u.getProviders(), u.getRoleList());
        }
    }

