package com.contactmgmtsystem.full_stack_contactMgmtSys.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

//    user create and login dynamically using java code with memory service
//    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @Bean
    public UserDetailsService userDetailsService() {
/* Things to remember: This User is imported from spring.security
        We can also assign role for each member: user, client, admin or guest
        */
        UserDetails user1 = User
                .withUsername("root")
                .password("root")
                .roles("ADMIN", "USER")
                .build();

        var inMemoryUserDetailsManger = new InMemoryUserDetailsManager(user1);
        return inMemoryUserDetailsManger;
    }

}
