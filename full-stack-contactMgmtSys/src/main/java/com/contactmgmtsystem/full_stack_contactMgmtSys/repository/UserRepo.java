package com.contactmgmtsystem.full_stack_contactMgmtSys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;

// Repo are used to interact with db
@Repository
public interface UserRepo extends JpaRepository<User, String> {

}
