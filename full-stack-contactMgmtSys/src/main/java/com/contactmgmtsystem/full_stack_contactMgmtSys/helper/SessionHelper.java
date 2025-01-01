package com.contactmgmtsystem.full_stack_contactMgmtSys.helper;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SessionHelper {


    public static void removeMessage() {
//        This is request the content to hold until session ends firtly it will get the reqAttr and for that first it will get the requst and get the session using servletRequest
        try {
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        }catch (Exception e) {
            System.out.println("Error in session"+ e);
        }
    }
}
