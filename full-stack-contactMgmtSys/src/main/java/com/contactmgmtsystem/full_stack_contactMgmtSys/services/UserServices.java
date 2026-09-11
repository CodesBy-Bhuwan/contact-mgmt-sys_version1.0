package com.contactmgmtsystem.full_stack_contactMgmtSys.services;

import java.util.List;
import java.util.Optional;

import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;

public interface UserServices {

    User saveUser(User user);
    Optional<User> getUserById(String id);
    Optional<User> updateUser(User user);
    void deleteUser(String id);
    boolean isUserExist(String userId);
    boolean isUserExistByEmail(String email);   // FIX: was "isUSerExistByEmail"
    List<User> getAllUsers();
    User getUserByEmail(String email);
    void updatePassword(String userId, String rawPassword);
    void setUserEnabled(String userId, boolean enabled);
}