package com.contactmgmtsystem.full_stack_contactMgmtSys.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.Contact;

@Repository
public interface ContactRepo extends JpaRepository<Contact, String> {

    List<Contact> findByUserUserIdOrderByNameAsc(String userId);

    // THE security method: id alone is never enough — must also match the owner
    Optional<Contact> findByIdAndUserUserId(String id, String userId);

    long countByUserUserId(String userId);

    @Query("""
        select c from Contact c
        where c.user.userId = :userId
          and (lower(c.name) like lower(concat('%', :q, '%'))
            or lower(c.email) like lower(concat('%', :q, '%'))
            or lower(c.username) like lower(concat('%', :q, '%'))
            or c.phoneNumber like concat('%', :q, '%'))
        order by lower(c.name)
        """)
    List<Contact> search(@Param("userId") String userId, @Param("q") String q);
}