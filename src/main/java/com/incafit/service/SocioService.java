package com.incafit.service;

import com.incafit.Model.Socio;
import java.util.List;

public interface SocioService {
    List<Socio> obtenerTodosSocios();
    Socio obtenerSocioPorId(Long id);
    void guardarSocio(Socio socio);
    void eliminarSocio(Long id);
}