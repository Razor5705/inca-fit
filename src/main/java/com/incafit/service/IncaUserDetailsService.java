package com.incafit.service;

import com.incafit.Model.Socio;
import com.incafit.Repository.SocioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

// IncaUserDetailsService.java
@Service
public class IncaUserDetailsService implements UserDetailsService {

    private final SocioRepository socioRepository;

    public IncaUserDetailsService(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Socio socio = socioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new User(
                socio.getEmail(),
                socio.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + socio.getRol().name()))
        );
    }
}