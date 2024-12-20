package com.contactmgmtsystem.full_stack_contactMgmtSys.services;

import java.util.List;
import java.util.Optional;

import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;

public interface UserServices {

    User saveUser(User user);
    // Whenever we want to cl getUserById, then we will get optional data. From this we can see if there already existed data.
    Optional<User> getUserById(String id);
    Optional<User> updateUser(User user);
    void deleteUser(String id);
    boolean isUserExist(String userId);
    boolean isUSerExistByEmail(String email);
    List<User>getAllUsers();

}
