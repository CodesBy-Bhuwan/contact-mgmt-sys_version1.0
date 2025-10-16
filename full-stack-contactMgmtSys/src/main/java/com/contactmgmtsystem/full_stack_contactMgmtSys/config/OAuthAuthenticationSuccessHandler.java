package com.contactmgmtsystem.full_stack_contactMgmtSys.config;

import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.Providers;
import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;
import com.contactmgmtsystem.full_stack_contactMgmtSys.helper.AppConst;
import com.contactmgmtsystem.full_stack_contactMgmtSys.repository.UserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        logger.info("OAuthAuthenticationSuccessHandler");

        var oauth2authenticationToken = (OAuth2AuthenticationToken)authentication;
        String authorizedCliendRedistrationId = oauth2authenticationToken.getAuthorizedClientRegistrationId();
        logger.info(authorizedCliendRedistrationId);

        var oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

//        Setting rules to take into record from the login
        oauthUser.getAttributes().forEach((key, value) -> {
            logger.info(key + ": " + value);
        });

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setRoleList(List.of(AppConst.ROLE_USER));
        user.setEmailVerified(true);
        user.setEnabled(true);

        //        For multiple login with social media -> platforms
        if (authorizedCliendRedistrationId.equalsIgnoreCase("google")) {
//            Google
//            What data to be taken from login with google
            user.setEmail(oauthUser.getAttribute("email").toString());
            user.setProfilePic(oauthUser.getAttribute("profile_pic").toString());
            user.setName(oauthUser.getAttribute("name").toString());
            user.setProviderUserId(oauthUser.getName());
            user.setProviders(Providers.GOOGLE);
            user.setAbout("This account is created using google");

        }else if (authorizedCliendRedistrationId.equalsIgnoreCase("facebook")) {
//            Facebook
//            Here we might not get email so we will use username@fbook.com if user's email is not found
            String email = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email") : oauthUser.getAttribute("login").toString() + "@fbook.com";
            user.setEmail(email);
            String picture = oauthUser.getAttribute("avatar_url").toString();
            String name = oauthUser.getAttribute("login").toString();
            String providerUserId = oauthUser.getName();

            user.setEmail(email);
            user.setProfilePic(picture);
            user.setName(name);
            user.setProviderUserId(providerUserId);
//            user.setProviders(Providers.FACEBOOK);
            user.setAbout("This account is created using facebook");


        }else if (authorizedCliendRedistrationId.equalsIgnoreCase("github")) {
//            Github
            String email = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email") : oauthUser.getAttribute("login").toString() + "@git.com";
            user.setEmail(email);
            String picture = oauthUser.getAttribute("avatar_url").toString();
            String name = oauthUser.getAttribute("login").toString();
            String providerUserId = oauthUser.getName();

            user.setEmail(email);
            user.setProfilePic(picture);
            user.setName(name);
            user.setProviderUserId(providerUserId);
            user.setProviders(Providers.GITHUB);
            user.setAbout("This account is created using github");


        }else {
//            Others
            logger.info("OAuthAuthenticationSuccessHandler: Unknown Provider");
        }



       /*
        DefaultOAuth2User user = (DefaultOAuth2User)authentication.getPrincipal();
/*   --------This is to get all the user's information---------
        logger.info(user.getName()); //will console username

        user.getAttributes().forEach((key, value) ->{ logger.info(key + " => " + value);
        }); // Will provide all the attributes like firstName, lastName, language, time, profilePicture, all the details set in google cloud console

        logger.info(user.getAuthorities().toString());
 ----------*
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

        // Logic: If user already exist don't create user instead use existed user to login and create new user if doesn't exist


*/
//      Will redirect to user's profile

        User user2= userRepo.findByEmail(user.getEmail()).orElse(null);
        if (user2 == null){
            userRepo.save(user);
        }
        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");


    }
}
