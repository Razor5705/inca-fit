package com.incafit.Config;

import com.incafit.service.IncaUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@Profile("dev")
public class DevUserDetailsConfig {

    @Bean
    @Primary
    public UserDetailsService devUserDetailsService(IncaUserDetailsService delegate) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        UserDetails tutorAdmin = User.withUsername("tutor.admin@incafit.com")
                .password(encoder.encode("Tutor123*"))
                .roles("ADMIN")
                .build();

        return username -> {
            if (username.equalsIgnoreCase(tutorAdmin.getUsername())) {
                return tutorAdmin;
            }
            return delegate.loadUserByUsername(username);
        };
    }
}