package com.contactmgmtsystem.full_stack_contactMgmtSys.implementation;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;
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
        return userRepo.save(user);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> updatUser(User user) {
        
        userRepo.findById(user.getUserId()).orElseThrow(()-> new ResourceNotFoundException());
    }

    @Override
    public void deleteUser(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public boolean isUserExist(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isUserExist'");
    }

    @Override
    public boolean isUSerExistByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isUSerExistByEmail'");
    }

    @Override
    public List<User> getAllUsers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllUsers'");
    }





}
