package com.incafit.service;



import com.incafit.Model.Socio;

public interface SocioService {
    boolean existeEmail(String email);
    void guardarSocio(Socio socio);
}