package com.contactmgmtsystem.full_stack_contactMgmtSys.services;

import java.util.List;

import com.contactmgmtsystem.full_stack_contactMgmtSys.dto.ContactDto;
import com.contactmgmtsystem.full_stack_contactMgmtSys.forms.ContactForm;

public interface ContactService {
    List<ContactDto> getAll(String email);
    List<ContactDto> getAllByUserId(String userId);  // admin only
    ContactDto get(String email, String id);
    ContactDto create(String email, ContactForm form);
    ContactDto update(String email, String id, ContactForm form);
    void delete(String email, String id);
    List<ContactDto> search(String email, String q);
    ContactDto toggleFav(String email, String id);
    String revealPassword(String email, String id);
    long countByUserId(String userId);               // admin stats
}