package com.contactmgmtsystem.full_stack_contactMgmtSys.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // REMOVED: findByEmailAndPassword — impossible with BCrypt hashes and
    // it tempts plaintext-password lookups. Login verification happens via
    // DaoAuthenticationProvider + passwordEncoder.matches().
}