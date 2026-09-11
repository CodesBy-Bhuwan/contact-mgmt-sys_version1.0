package com.contactmgmtsystem.full_stack_contactMgmtSys.helper;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SessionHelper {

    public static void removeMessage() {
        try {
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest().getSession();
            session.removeAttribute("message");   // FIX: was fetched but never removed
        } catch (Exception e) {
            System.out.println("Error in session: " + e);
        }
    }
}