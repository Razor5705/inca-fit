package com.incafit.service;

import com.incafit.Model.Socio;
import java.util.List;

public interface SocioService {
    List<Socio> obtenerTodosSocios();
    Socio obtenerSocioPorId(Long id);
    void guardarSocio(Socio socio);
    void eliminarSocio(Long id);
    void cambiarEstadoSocio(Long id, boolean estado);
    boolean existeEmail(String email); // Añade esta línea

}