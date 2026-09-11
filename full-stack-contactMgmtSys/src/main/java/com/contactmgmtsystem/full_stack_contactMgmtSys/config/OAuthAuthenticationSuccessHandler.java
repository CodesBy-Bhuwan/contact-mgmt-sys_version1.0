package com.contactmgmtsystem.full_stack_contactMgmtSys.config;

import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.Providers;
import com.contactmgmtsystem.full_stack_contactMgmtSys.entities.User;
import com.contactmgmtsystem.full_stack_contactMgmtSys.helper.AppConst;
import com.contactmgmtsystem.full_stack_contactMgmtSys.repository.UserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Value("${app.oauth2.success-redirect:/user/dashboard}")
    private String successRedirect;
    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
        var oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setRoleList(List.of(AppConst.ROLE_USER));
        user.setEnabled(true);
        user.setEmailVerified(true);

        switch (registrationId.toLowerCase()) {
            case "google" -> {
                user.setEmail(oauthUser.getAttribute("email"));
                user.setName(oauthUser.getAttribute("name"));
                user.setProfilePic(oauthUser.getAttribute("picture"));
                user.setProviderUserId(oauthUser.getName());
                user.setProviders(Providers.GOOGLE);
                user.setAbout("This account is created using google");
            }
            case "github" -> {
                String login = oauthUser.getAttribute("login");
                String email = oauthUser.getAttribute("email");          // often null on GitHub
                user.setEmail(email != null ? email : login + "@git.com");
                user.setName(oauthUser.getAttribute("name") != null ? oauthUser.getAttribute("name") : login);
                user.setProfilePic(oauthUser.getAttribute("avatar_url"));
                user.setProviderUserId(oauthUser.getName());
                user.setProviders(Providers.GITHUB);
                user.setAbout("This account is created using github");
            }
            case "facebook" -> {
                String fbId = oauthUser.getAttribute("id");
                String email = oauthUser.getAttribute("email");
                user.setEmail(email != null ? email : fbId + "@facebook.com");
                user.setName(oauthUser.getAttribute("name"));
                user.setProfilePic(extractFacebookPicture(oauthUser.getAttribute("picture")));
                user.setProviderUserId(oauthUser.getName());
                user.setProviders(Providers.FACEBOOK);
                user.setAbout("This account is created using facebook");
            }
            default -> logger.warn("Unknown OAuth provider: {}", registrationId);
        }

        // If this email already exists (self-registered or other provider), reuse the account
        User existing = userRepo.findByEmail(user.getEmail()).orElse(null);
        if (existing == null) {
            userRepo.save(user);
        }

//        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");
        new DefaultRedirectStrategy().sendRedirect(request, response, "http://localhost:5173/");
    }

    // With fields=id,name,email,picture Facebook returns nested JSON: picture.data.url
    private String extractFacebookPicture(Object picture) {
        try {
            if (picture instanceof Map<?, ?> p) {
                Object data = p.get("data");
                if (data instanceof Map<?, ?> d && d.get("url") != null) {
                    return d.get("url").toString();
                }
            }
        } catch (Exception e) {
            logger.warn("Could not read facebook picture attribute", e);
        }
        return null;
    }
}