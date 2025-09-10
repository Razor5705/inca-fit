package com.incafit.service;

import com.incafit.Model.Rol;
import com.incafit.Model.Socio;
import com.incafit.Repository.SocioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

// IncaUserDetailsService.java
@Service
@Transactional
public class IncaUserDetailsService implements UserDetailsService {

    private final SocioRepository socioRepository;


    public IncaUserDetailsService(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Socio socio = socioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Asegúrate de que los roles tengan el prefijo "ROLE_"
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + socio.getRol().name())
        );

        return new User(
                socio.getEmail(),
                socio.getPassword(),
                authorities
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Rol rol) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }
}