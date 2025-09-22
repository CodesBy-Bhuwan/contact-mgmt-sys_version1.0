package com.contactmgmtsystem.full_stack_contactMgmtSys.config;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = (Logger) LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        logger.info("OAuthAuthenticationSuccessHandler");

        DefaultOAuth2User user = (DefaultOAuth2User)authentication.getPrincipal();

        logger.info(user.getName()); //will console username

        user.getAttributes().forEach((key, value) ->{ logger.info(key + " => " + value);
        }); // Will provide all the attributes like firstName, lastName, language, time, profilePicture, all the details set in google cloud console

        logger.info(user.getAuthorities().toString());

//        Will redirect to user's profile
        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");

        
    }
}
