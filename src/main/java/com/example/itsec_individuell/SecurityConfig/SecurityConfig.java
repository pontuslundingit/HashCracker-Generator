package com.example.itsec_individuell.SecurityConfig;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers( "/userlogin", "/register", "/css/**", "/js/**").permitAll().anyRequest().authenticated()
                )
                .oauth2Login((form) -> form
                .loginPage("/userlogin")
                .defaultSuccessUrl("/home", true)
                .permitAll())
                .formLogin((form) -> form
                        .loginPage("/userlogin")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/userlogin?logout")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .permitAll()
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                );

        return http.build();
    }

//    private GrantedAuthoritiesMapper userAuthoritiesMapper() {
//        return (authorities) -> {
//            List<SimpleGrantedAuthority> mappedAuthorities = new ArrayList<>();
//
//            authorities.forEach(authority -> {
//                if (authority instanceof OAuth2UserAuthority oAuth2UserAuthority) {
//                    Map<String, Object> userAttributes = oAuth2UserAuthority.getAttributes();
//
//                    String login = userAttributes.get("login").toString();
//
//
//                }
//            });
//        }
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}
