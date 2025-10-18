package com.contactmgmtsystem.full_stack_contactMgmtSys.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.security.Principal;

public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication) {


        if (authentication instanceof OAuth2AuthenticationToken) {


            var aOAuth2AuthenticationToken=(OAuth2AuthenticationToken)authentication;
            var clientId = aOAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oauth2User = (OAuth2User)authentication.getPrincipal();
            String username = "";

//        If User Logs in using Google
            if(clientId.equalsIgnoreCase("google")){
                System.out.println("Logged in via Google");
                username=oauth2User.getAttribute("email").toString();
            }

//        If User Logs in using Facebook
            else if(clientId.equalsIgnoreCase("facebook")){
                System.out.println("Logged in via facebook");
                username = oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email").toString() : oauth2User.getAttribute("login").toString() + "@fbook.com";
            }

//        If User Logs in using Github
            else if(clientId.equalsIgnoreCase("Github")){
                System.out.println("Logged in via Github");
                username = oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email").toString() : oauth2User.getAttribute("login").toString() + "@git.com";
            }
            return username;

//        If User Logs in manually
        } else {
            System.out.println("Manally Logged In");
            return authentication.getName();
        }


    }

}
