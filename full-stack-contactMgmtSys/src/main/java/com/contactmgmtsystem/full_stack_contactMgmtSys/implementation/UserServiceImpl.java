package com.contactmgmtsystem.full_stack_contactMgmtSys.implementation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;
import com.contactmgmtsystem.full_stack_contactMgmtSys.helper.ResourceNotFoundException;
import com.contactmgmtsystem.full_stack_contactMgmtSys.repository.UserRepo;
import com.contactmgmtsystem.full_stack_contactMgmtSys.services.UserServices;

@Service
public class UserServiceImpl implements UserServices {
    
    // To save these we will use Repository
    @Autowired
    private UserRepo userRepo;

    // With property-injection we can also use constructor-injection for that we will create parameterized constructor


    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public User saveUser(User user) {
        // Before saving we need to generate dynamic user id
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);


//        user.setProfilePic("userId");
        return userRepo.save(user);

    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        
        User user2=userRepo.findById(user.getUserId()).orElseThrow(()-> new ResourceNotFoundException("User Not Found"));
        // Now we have to update user2 with user
        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePic(user.getProfilePic());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneNumberVerified(user.isPhoneNumberVerified());
        user2.setProviders(user.getProviders());
        user2.setProviderUserId(user.getProviderUserId());

        // Now these data has to be saved in db
        User save = userRepo.save(user2);
        return Optional.ofNullable(save);
    }

    @Override
    public void deleteUser(String id) {
        // To deleteUser we need to fetch the data first, here we will fetch the data using id(primary key)
        User user2=userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User Not Found"));
        userRepo.delete(user2);

    }

    @Override
    public boolean isUserExist(String userId) {
        User user2=userRepo.findById(userId).orElse(null);
        return user2!=null ? true : false;
    }

    @Override
    public boolean isUSerExistByEmail(String email) {
        // We can use custom find methods for that we will make optional method in UserRepo to find User using Email
        User user = userRepo.findByEmail(email).orElse(null);
        return user!=null ? true : false;
    }

    @Override
    public List<User> getAllUsers() {
        // To find all the users we can use findAll() mehtod
        return userRepo.findAll();
    }





}
