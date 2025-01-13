package com.contactmgmtsystem.full_stack_contactMgmtSys.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

//   For each User or admin or guest can access with their own username and password and nobody can access other info without permission
    @Bean
    public AuthenticationProvider authenticationProvieder(UserDetailsPasswordService userDetailsPasswordService){

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//      Need to pass UserDetailsService Object
        daoAuthenticationProvider.setUserDetailsService(null);
//      Need to pass PasswordEncoder Object
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());


        return daoAuthenticationProvider;
    }

//    Creating Bean just to produce object for above daoAuthenticaitonProvider
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
