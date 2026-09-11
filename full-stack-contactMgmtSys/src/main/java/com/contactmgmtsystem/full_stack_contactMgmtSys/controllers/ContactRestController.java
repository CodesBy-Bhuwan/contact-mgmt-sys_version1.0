package com.contactmgmtsystem.full_stack_contactMgmtSys.controllers;

import java.util.List;
import java.util.Map;

import com.contactmgmtsystem.full_stack_contactMgmtSys.dto.ContactDto;
import com.contactmgmtsystem.full_stack_contactMgmtSys.forms.ContactForm;
import com.contactmgmtsystem.full_stack_contactMgmtSys.services.ContactService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacts")
public class ContactRestController {

    @Autowired
    private ContactService contactService;

    // Authentication.getName() = email (User.getUsername()); all scoping happens in the service
    private String me(Authentication auth) { return auth.getName(); }

    @GetMapping
    public List<ContactDto> all(Authentication auth) {
        return contactService.getAll(me(auth));
    }

    @GetMapping("/search")
    public List<ContactDto> search(@RequestParam("q") String q, Authentication auth) {
        return contactService.search(me(auth), q);
    }

    @GetMapping("/{id}")
    public ContactDto one(@PathVariable String id, Authentication auth) {
        return contactService.get(me(auth), id);
    }

    @PostMapping
    public ResponseEntity<ContactDto> create(@Valid @RequestBody ContactForm form, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contactService.create(me(auth), form));
    }

    @PutMapping("/{id}")
    public ContactDto update(@PathVariable String id, @Valid @RequestBody ContactForm form, Authentication auth) {
        return contactService.update(me(auth), id, form);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, Authentication auth) {
        contactService.delete(me(auth), id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/fav")
    public ContactDto toggleFav(@PathVariable String id, Authentication auth) {
        return contactService.toggleFav(me(auth), id);
    }

    // The ONLY endpoint that decrypts. Scoped to the owner, like everything else.
    @GetMapping("/{id}/password")
    public Map<String, String> revealPassword(@PathVariable String id, Authentication auth) {
        return Map.of("password", contactService.revealPassword(me(auth), id));
    }
}