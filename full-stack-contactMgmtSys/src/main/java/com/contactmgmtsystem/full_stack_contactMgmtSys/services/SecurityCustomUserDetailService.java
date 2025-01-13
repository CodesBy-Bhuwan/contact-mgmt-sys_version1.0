package com.contactmgmtsystem.full_stack_contactMgmtSys.services;

import com.contactmgmtsystem.full_stack_contactMgmtSys.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityCustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
//        Load custom user by itself dynamically

        return userRepo
                .findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found :" + username));
    }
}
