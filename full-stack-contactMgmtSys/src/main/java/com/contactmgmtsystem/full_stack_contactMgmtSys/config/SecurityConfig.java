package com.contactmgmtsystem.full_stack_contactMgmtSys.config;


import com.contactmgmtsystem.full_stack_contactMgmtSys.services.SecurityCustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


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
//        How route is done??
        httpSecurity.authorizeRequests(authorize->{
//            authorize.requestMatchers("/home","/register","/services").permitAll(); this will authorize without login
            authorize.requestMatchers("/user/**").authenticated(); //url starting with /user will authorize
            authorize.anyRequest().permitAll();//other request will require authorization

        });
//this is default login || if any changes has to be made related to form-login then it is done through this customizer
        httpSecurity.formLogin(formLogin->{
/*           Here instead of using SpringSecurity Login page we can use our customized login form by passing login URL
            fromLogin.loginPage("/login")
                    .loginProcessingUrl("/authenticate");
                    This chain will use our login page but processing and submission will be in authentiacte page
                    */
//            This is normal practice but chaining method is widely used
            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.successForwardUrl("/user/dashboard");
//            formLogin.failureUrl("/login?error=true");
            formLogin.usernameParameter("email"); //This will make our username in login page be email instead of username
            formLogin.passwordParameter("password");
/*            Handling if failure is occured
            fromLogin.failureHandler(new AuthenticationFailureHandler() {
                @Override
                public void onAuthenticationFailure(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    AuthenticationException exception) throws IOException, ServletException {

                    throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationFailure'");

                }
            });
            fromLogin.successHandler(new AuthenticationSuccessHandler() {

                @Override
                public void onAuthenticationSuccess(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    Authentication authentication) throws IOException, ServletException {
                    throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationSuccess'");

                }
            });
            */

        });
//        When user want or will get loggedout.
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logoutForm->{
            logoutForm.logoutUrl("/do-logout");
//            logoutForm.logoutSuccessUrl("/login?logout=true");
        });

        return httpSecurity.build();
    }

//    Creating Bean just to produce object for above daoAuthenticaitonProvider
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
