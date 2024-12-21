package com.contactmgmtsystem.full_stack_contactMgmtSys.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;

// Repo are used to interact with db
@Repository
public interface UserRepo extends JpaRepository<User, String> {



    // This optional custom method is to find using User using Email keeping email args
    Optional<User>findByEmail(String email);

    /*In case of creating such optional method to find User using 2 arguments:
    Eg.: Using Email and Password
    Optional<User>findByEmailAndPassword(String email, String password); */

}
