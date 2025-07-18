// SocioServiceImpl.java
package com.incafit.service;

import com.incafit.Model.Socio;
import com.incafit.Repository.SocioRepository;
import org.springframework.stereotype.Service;

@Service
public class SocioServiceImpl implements SocioService {

    private final SocioRepository socioRepository;

    public SocioServiceImpl(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    @Override
    public boolean existeEmail(String email) {
        return socioRepository.findByEmail(email).isPresent();
    }

    @Override
    public void guardarSocio(Socio socio) {
        socioRepository.save(socio);
    }
}