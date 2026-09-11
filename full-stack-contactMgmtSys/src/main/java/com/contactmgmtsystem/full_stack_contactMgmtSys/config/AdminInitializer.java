package com.contactmgmtsystem.full_stack_contactMgmtSys.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.contactmgmtsystem.full_stack_contactMgmtSys.helper.AppConst;
import com.contactmgmtsystem.full_stack_contactMgmtSys.repository.UserRepo;

@Component
public class AdminInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AdminInitializer.class);

    @Autowired private UserRepo userRepo;

    @Value("${app.admin.email:}")
    private String adminEmail;

    @Override
    public void run(String... args) {
        if (adminEmail == null || adminEmail.isBlank()) return;
        userRepo.findByEmail(adminEmail.trim()).ifPresent(u -> {
            if (!u.getRoleList().contains(AppConst.ROLE_ADMIN)) {
                u.getRoleList().add(AppConst.ROLE_ADMIN);
                userRepo.save(u);
                logger.info("Promoted {} to ADMIN", adminEmail);
            }
        });
    }
}