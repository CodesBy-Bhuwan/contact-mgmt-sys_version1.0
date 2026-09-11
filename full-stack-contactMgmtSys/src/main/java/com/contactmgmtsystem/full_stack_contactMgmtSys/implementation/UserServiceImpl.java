package com.contactmgmtsystem.full_stack_contactMgmtSys.implementation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.contactmgmtsystem.full_stack_contactMgmtSys.helper.AppConst;
import com.contactmgmtsystem.full_stack_contactMgmtSys.helper.ResourceNotFoundException;
import com.contactmgmtsystem.full_stack_contactMgmtSys.repository.UserRepo;
import com.contactmgmtsystem.full_stack_contactMgmtSys.services.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;

@Service
public class UserServiceImpl implements UserServices {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        user.setUserId(UUID.randomUUID().toString());

        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        user.setRoleList(List.of(AppConst.ROLE_USER));
        logger.info("Saving user, provider: {}", user.getProviders());
        return userRepo.save(user);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User user2 = userRepo.findById(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePic(user.getProfilePic());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneNumberVerified(user.isPhoneNumberVerified());
        user2.setProviders(user.getProviders());
        user2.setProviderUserId(user.getProviderUserId());

        // FIX: only overwrite the password when a new one was actually supplied,
        // and encode it. The old code stored whatever came in — raw or null.
//        if (user.getPassword() != null && !user.getPassword().isBlank()) {
//            user2.setPassword(passwordEncoder.encode(user.getPassword()));
//        }

        return Optional.ofNullable(userRepo.save(user2));
    }

    @Override
    public void deleteUser(String id) {
        User user2 = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        userRepo.delete(user2);
    }

    @Override
    public boolean isUserExist(String userId) {
        return userRepo.existsById(userId);   // FIX: no entity load needed
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        return userRepo.existsByEmail(email); // FIX: same
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public void updatePassword(String userId, String rawPassword) {
        User u = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        u.setPassword(passwordEncoder.encode(rawPassword));
        userRepo.save(u);
    }

    @Override
    public void setUserEnabled(String userId, boolean enabled) {
        User u = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        u.setEnabled(enabled);   // false = soft delete: Spring Security blocks login via isEnabled()
        userRepo.save(u);
    }
}