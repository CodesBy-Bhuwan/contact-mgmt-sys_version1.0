package com.contactmgmtsystem.full_stack_contactMgmtSys.config;


import com.contactmgmtsystem.full_stack_contactMgmtSys.services.SecurityCustomUserDetailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@Configuration
public class SecurityConfig {

//    user create and login dynamically using java code with memory service
//    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    /*
   #########This bean shows how we can assign univeral or default username and password for everyUser

    @Bean
    public UserDetailsService userDetailsService() {
// Things to remember: This User is imported from spring.security
//        We can also assign role for each member: user, client, admin or guest


        UserDetails user1 = User
                .withDefaultPasswordEncoder()
                .username("root")
                .password("root")
                .roles("ADMIN", "USER")
                .build();

        UserDetails user2 = User
                .withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("Guest")
                .build();

        var inMemoryUserDetailsManger = new InMemoryUserDetailsManager(user1, user2);
        return inMemoryUserDetailsManger;
    }
    */

    @Autowired
    private SecurityCustomUserDetailService customUserDetailService;


    //   For each User or admin or guest can access with their own username and password and nobody can access other info without permission
    @Bean
    public DaoAuthenticationProvider authenticationProvieder(){

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//      Need to pass UserDetailsService Object
        daoAuthenticationProvider.setUserDetailsService(customUserDetailService);
//      Need to pass PasswordEncoder Object
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());


        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

//        Configuration
        httpSecurity.authorizeRequests(authorize->{
//            authorize.requestMatchers("/home","/register","/services").permitAll(); this will authorize without login
            authorize.requestMatchers("/user/**").authenticated(); //url starting with /user will authorize
            authorize.anyRequest().permitAll();//other request will require authorization

        });
//this is default login || if any changes has to be made related to form-login then it is done through this customizer
        httpSecurity.formLogin(fromLogin->{
/*           Here instead of using SpringSecurity Login page we can use our customized login form by passing login URL
            fromLogin.loginPage("/login")
                    .loginProcessingUrl("/authenticate");
                    This chain will use our login page but processing and submission will be in authentiacte page
                    */
//            This is normal practice but chaining method is widely used
            fromLogin.loginPage("/login");
            fromLogin.loginProcessingUrl("/authenticate");
            fromLogin.successForwardUrl("/user/dashboard");
            fromLogin.failureUrl("/login?error=true");
            fromLogin.usernameParameter("email"); //This will make our username in login page be email instead of username
            fromLogin.passwordParameter("password");
//            Handling if failure is occured
            fromLogin.failureHandler(new AuthenticationFailureHandler() {
                @Override
                public void onAuthenticationFailure(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    AuthenticationException exception) throws IOException, ServletException {

                    throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationFailure'");

                }
            });

        });

        return httpSecurity.build();
    }

//    Creating Bean just to produce object for above daoAuthenticaitonProvider
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
