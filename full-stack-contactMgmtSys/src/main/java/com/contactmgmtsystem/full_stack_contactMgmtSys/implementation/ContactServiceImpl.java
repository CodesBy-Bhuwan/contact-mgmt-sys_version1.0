package com.contactmgmtsystem.full_stack_contactMgmtSys.implementation;

import java.util.List;
import java.util.UUID;

import com.contactmgmtsystem.full_stack_contactMgmtSys.dto.ContactDto;
import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.Contact;
import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;
import com.contactmgmtsystem.full_stack_contactMgmtSys.forms.ContactForm;
import com.contactmgmtsystem.full_stack_contactMgmtSys.helper.EncryptionUtil;
import com.contactmgmtsystem.full_stack_contactMgmtSys.helper.ResourceNotFoundException;
import com.contactmgmtsystem.full_stack_contactMgmtSys.repository.ContactRepo;
import com.contactmgmtsystem.full_stack_contactMgmtSys.services.ContactService;
import com.contactmgmtsystem.full_stack_contactMgmtSys.services.UserServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired private ContactRepo contactRepo;
    @Autowired private UserServices userServices;
    @Autowired private EncryptionUtil encryptionUtil;

    private User owner(String email) {
        return userServices.getUserByEmail(email);
    }

    // Every access goes through this: id + owner must BOTH match (IDOR protection)
    private Contact owned(String email, String id) {
        return contactRepo.findByIdAndUserUserId(id, owner(email).getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
    }

    @Override
    public List<ContactDto> getAll(String email) {
        return contactRepo.findByUserUserIdOrderByNameAsc(owner(email).getUserId())
                .stream().map(ContactDto::from).toList();
    }

    @Override
    public List<ContactDto> getAllByUserId(String userId) {
        return contactRepo.findByUserUserIdOrderByNameAsc(userId)
                .stream().map(ContactDto::from).toList();
    }

    @Override
    public ContactDto get(String email, String id) {
        return ContactDto.from(owned(email, id));
    }

    @Override
    public ContactDto create(String email, ContactForm form) {
        Contact c = Contact.builder()
                .id(UUID.randomUUID().toString())
                .name(form.getName())
                .username(form.getUsername())
                .email(form.getEmail())
                .phoneNumber(form.getPhoneNumber())
                .address(form.getAddress())
                .description(form.getDescription())
                .webLink(form.getWebLink())
                .facebookLink(form.getFacebookLink())
                .user(owner(email))
                .build();
        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            c.setEncryptedPassword(encryptionUtil.encrypt(form.getPassword()));
        }
        return ContactDto.from(contactRepo.save(c));
    }

    @Override
    public ContactDto update(String email, String id, ContactForm form) {
        Contact c = owned(email, id);
        c.setName(form.getName());
        c.setUsername(form.getUsername());
        c.setEmail(form.getEmail());
        c.setPhoneNumber(form.getPhoneNumber());
        c.setAddress(form.getAddress());
        c.setDescription(form.getDescription());
        c.setWebLink(form.getWebLink());
        c.setFacebookLink(form.getFacebookLink());
        // blank password on update = keep the stored one
        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            c.setEncryptedPassword(encryptionUtil.encrypt(form.getPassword()));
        }
        return ContactDto.from(contactRepo.save(c));
    }

    @Override
    public void delete(String email, String id) {
        contactRepo.delete(owned(email, id));
    }

    @Override
    public List<ContactDto> search(String email, String q) {
        return contactRepo.search(owner(email).getUserId(), q)
                .stream().map(ContactDto::from).toList();
    }

    @Override
    public ContactDto toggleFav(String email, String id) {
        Contact c = owned(email, id);
        c.setFav(!c.isFav());
        return ContactDto.from(contactRepo.save(c));
    }

    @Override
    public String revealPassword(String email, String id) {
        Contact c = owned(email, id);
        if (c.getEncryptedPassword() == null) {
            throw new ResourceNotFoundException("No password stored for this contact");
        }
        return encryptionUtil.decrypt(c.getEncryptedPassword());
    }

    @Override
    public long countByUserId(String userId) {
        return contactRepo.countByUserUserId(userId);
    }
}