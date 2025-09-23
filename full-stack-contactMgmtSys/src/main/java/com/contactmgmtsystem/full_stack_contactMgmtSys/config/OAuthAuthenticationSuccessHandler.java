package com.contactmgmtsystem.full_stack_contactMgmtSys.config;

import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.Providers;
import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;
import com.contactmgmtsystem.full_stack_contactMgmtSys.helper.AppConst;
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
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;

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
/*   --------This is to get all the user's information---------
        logger.info(user.getName()); //will console username

        user.getAttributes().forEach((key, value) ->{ logger.info(key + " => " + value);
        }); // Will provide all the attributes like firstName, lastName, language, time, profilePicture, all the details set in google cloud console

        logger.info(user.getAuthorities().toString());
 */
        //-------In case we want only some specific user's data to be recorded-----
        String email = user.getAttribute("email").toString();
        String name = user.getAttribute("name").toString();
//        String password = user.getAttribute("password").toString();
//        String token = user.getAttribute("token").toString();
        String picture = user.getAttribute("picture").toString();

        //------Create User and save to database

        User user1 = new User();

        user1.setEmail(email);
        user1.setName(name);
        user1.setProfilePic(picture);
        user1.setPassword("password");
        user1.setUserId(UUID.randomUUID().toString());
        user1.setProviders(Providers.GOOGLE);
        user1.setEnabled(true);

        user1.setEmailVerified(true);
        user1.setProviderUserId(user.getName());
        user1.setRoleList(List.of(AppConst.ROLE_USER));
        user1.setAbout("This account is creaed using google signup!");
        


//      Will redirect to user's profile
        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");


    }
}
